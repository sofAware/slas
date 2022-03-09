package kr.sofaware.slas.function.board;

import kr.sofaware.slas.entity.Syllabus;
import kr.sofaware.slas.service.BoardService;
import kr.sofaware.slas.entity.Board;
import kr.sofaware.slas.service.FileService;
import kr.sofaware.slas.service.MemberService;
import kr.sofaware.slas.service.SyllabusService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/p/" + NoticeProfessorController.ROOT_URL)
@RequiredArgsConstructor
public class NoticeProfessorController {

    public static final String ROOT_URL = "notice";
    private static final String TITLE = "\uD83D\uDCE2 공지사항";
    private final BoardService noticeService;

    private final MemberService memberService;
    private final SyllabusService syllabusService;
    private final FileService fileService;

    // 전체 공지사항 리스트
    @GetMapping
    public String readList(Model model, Principal principal,
                           @Nullable @RequestParam("year-semester") String yearSemester,
                           @Nullable @RequestParam("syllabus-id") String syllabusId) {

        // 교수가 강의한 학기와 과목들 조회
        Map<String, List<Syllabus>> lectures = syllabusService.mapAllByProfessorId(principal.getName());

        // 강의 선택 없으면
        if (syllabusId == null || syllabusId.isEmpty()) {
            // 학기 선택도 없으면 최근학기 전체 공지 선택
            if (yearSemester == null || yearSemester.isEmpty()) {
                ArrayList<String> yearSemesters = new ArrayList<>(lectures.keySet());
                // 이 사람이 했던 수업이 없을 경우 그냥 리턴
                if (yearSemesters.isEmpty())
                    return "board/list";

                // 있으면 최근 학기 입력
                yearSemester = yearSemesters.get(0);
            }
        }
        // 강의 선택이 있으면 학기는 학정번호에서 따오기
        else if (yearSemester == null) {
            yearSemester = syllabusId.substring(0, 4);
        }

        // 21-1를 2021학년도 1학기로 포맷하는 맵 초기화
        Map<String, String> formatYS = new TreeMap<>(Collections.reverseOrder());
        lectures.keySet().forEach(s -> formatYS.put(s, Syllabus.formatYearSemester(s)));

        // 학기 선택 리스트
        model.addAttribute("mapYS", formatYS);
        model.addAttribute("yearSemester", yearSemester);
        model.addAttribute("formatYS", Syllabus.formatYearSemester(yearSemester));

        // 강의 선택 리스트
        model.addAttribute("syllabuses", lectures.get(yearSemester));

        // 강의 선택 없으면 해당 학기 전체 강의에 대한 공지사항 긁어오기
        List<Board> boards = new ArrayList<>();
        if (syllabusId == null || syllabusId.isEmpty()) {
            lectures.get(yearSemester).forEach(syllabus ->
                    boards.addAll(noticeService.listAll(syllabus.getId())));

            // 템플릿에서 강의명과 강의시간을 표시하기 위해 (isEmpty 판별) 추가
            model.addAttribute("selectedSyllabusId", "");
            model.addAttribute("selectedSyllabusName", "전체");
        }
        else {
            boards.addAll(noticeService.listAll(syllabusId));

            // 선택된 강의 lectures에서 찾아서 강의명 입력
            Syllabus syllabus = lectures
                    .get(yearSemester)
                    .stream()
                    .filter(s -> s.getId().equals(syllabusId))
                    .findAny()
                    .get();
            model.addAttribute("selectedSyllabusId", syllabus.getId());
            model.addAttribute("selectedSyllabusName",
                    syllabus.getName() +
                            " (" + syllabus.formatClassTime() + ")");
        }

        // 날짜 내림차순 정렬 후 모델에 넣기
        boards.sort(Comparator.comparing(Board::getDate).reversed());
        model.addAttribute("boards", boards);
        model.addAttribute("rootURL", ROOT_URL);
        model.addAttribute("title", TITLE);

        return "board/list";
    }

    // 작성
    @GetMapping("write")
    public String getWriting(Model model, Principal principal,
                             @Nullable @RequestParam("syllabus-id") String syllabusId) {

        // 학정번호가 넘어왔으면 그걸로 강의 모델에 추가 아니면 교수한 강의 최근 1개 추가
        model.addAttribute("rootURL", ROOT_URL);
        model.addAttribute("title", TITLE);
        model.addAttribute("syllabus", syllabusId == null ?
                syllabusService.findFirstByProfessor_IdOrderByIdDesc(principal.getName()).get() :
                syllabusService.findById(syllabusId).get());

        return "/board/write";
    }
    @PostMapping("write")
    public String postWriting(BoardDto boardDto, Principal principal) throws IOException {

        // 작성 권한 없으면 403
        if (!syllabusService.existsByIdAndProfessor_Id(
                boardDto.getSyllabusId(),
                principal.getName()))
            return "error/403";

        // 게시글 만들기
        Board.BoardBuilder builder = Board.builder()
                .syllabus(syllabusService.findById(boardDto.getSyllabusId()).get())
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .member(memberService.loadUserByUsername(principal.getName()))
                .date(new Date())
                .view(0);

        // 첨부 파일 저장
        if (!boardDto.getFile().isEmpty()) {
            String attachmentPath = fileService.saveOnSyllabus(boardDto.getFile(), boardDto.getSyllabusId());
            builder
                    .attachmentName(boardDto.getFile().getOriginalFilename())
                    .attachmentPath(attachmentPath);
        }

        // 게시글 작성
        Board board = builder.build();
        noticeService.save(board);

        // 작성된 포스트 번호로 뷰 이동
        return "redirect:/p/" + ROOT_URL + "/" + board.getId();
    }

    // 열람
    @GetMapping("{boardIdStr:[0-9]+}")
    public String view(Model model, Principal principal,
                       @PathVariable String boardIdStr) {

        // 게시글 가져오기
        int boardId = Integer.parseInt(boardIdStr);
        Optional<Board> oBoard = noticeService.read(boardId);

        // 없으면 404
        if (oBoard.isEmpty())
            return "error/404";

        // 읽을 권한 없으면 403
        if (!syllabusService.existsByIdAndProfessor_Id(
                oBoard.get().getSyllabus().getId(),
                principal.getName()))
            return "error/403";

        // 조회 수 증가
        noticeService.increaseViewCount(boardId);

        // 열람
        model.addAttribute("rootURL", ROOT_URL);
        model.addAttribute("title", TITLE);
        model.addAttribute("board", oBoard.get());
        return "board/view";
    }

    // 수정
    @GetMapping("edit/{boardIdStr:[0-9]+}")
    public String GetEditing(Model model, Principal principal,
                             @PathVariable String boardIdStr) {

        // 게시글 가져오기
        int boardId = Integer.parseInt(boardIdStr);
        Optional<Board> oBoard = noticeService.read(boardId);

        // 없으면 404
        if (oBoard.isEmpty())
            return "error/404";

        // 글 작성자가 아니면 403
        if (!oBoard.get().getMember().getId().equals(principal.getName()))
            return "error/403";

        // 페이지 전송
        model.addAttribute("rootURL", ROOT_URL);
        model.addAttribute("title", TITLE);
        model.addAttribute("syllabus", oBoard.get().getSyllabus());
        model.addAttribute("board", oBoard.get());
        return "/board/write";
    }
    @PostMapping("edit/{boardIdStr:[0-9]+}")
    public String postEditing(BoardDto boardDto, Principal principal,
                              @PathVariable String boardIdStr) throws IOException {

        // 게시글 가져오기
        int boardId = Integer.parseInt(boardIdStr);
        Optional<Board> oBoard = noticeService.read(boardId);

        // 글 작성자가 아니면 403
        if (oBoard.isEmpty() ||
                !oBoard.get().getMember().getId().equals(principal.getName()))
            return "error/403";

        // 기본 board 값 가져오기
        Board board = oBoard.get();
        String attachmentName = board.getAttachmentName();
        String attachmentPath = board.getAttachmentPath();

        // 새로운 파일을 업로드하면
        if (!boardDto.getFile().isEmpty()) {
            if (attachmentName != null && !attachmentName.isEmpty())
                // 기존 파일 삭제
                fileService.deleteOnSyllabus(attachmentPath);

            attachmentName = boardDto.getFile().getOriginalFilename();
            attachmentPath = fileService.saveOnSyllabus(boardDto.getFile(), boardDto.getSyllabusId());
        }
        // 새로운 파일을 업로드 하지는 않았지만 게시글에 파일이 존재하고 파일 삭제를 원했다면 삭제!
        else if (attachmentName != null && !attachmentName.isEmpty() &&
                boardDto.getDeleteFile() != null && !boardDto.getDeleteFile().isEmpty()) {
            fileService.deleteOnSyllabus(attachmentPath);
            attachmentName = "";
            attachmentPath = "";
        }

        // 새로운 값들로 세팅
        board.update(boardDto.getTitle(), boardDto.getContent(), attachmentName, attachmentPath);
        noticeService.save(board);

        // 수정된 포스트 번호로 뷰 이동
        return "redirect:/p/" + ROOT_URL + "/" + boardIdStr;
    }

    // 삭제
    @GetMapping("delete/{boardIdStr:[0-9]+}")
    public String delete(Principal principal,
                         @PathVariable String boardIdStr) {

        // 게시글 가져오기
        int boardId = Integer.parseInt(boardIdStr);
        Optional<Board> oBoard = noticeService.read(boardId);

        // 없으면 404
        if (oBoard.isEmpty())
            return "error/404";

        // 글 작성자가 아니면 403
        if (!oBoard.get().getMember().getId().equals(principal.getName()))
            return "error/403";

        // 파일 및 게시글 삭제
        fileService.deleteOnSyllabus(oBoard.get().getAttachmentPath());
        noticeService.delete(boardId);

        // 목록으로 리디렉션
        return "redirect:/p/" + ROOT_URL;
    }
}
