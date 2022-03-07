package kr.sofaware.slas.board;

import kr.sofaware.slas.entity.Board;
import kr.sofaware.slas.entity.Syllabus;
import kr.sofaware.slas.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;

@Controller
@RequestMapping({ "/p/qna", "/s/qna" })
@RequiredArgsConstructor
public class QnaController {

    public static final String ROOT_URL = "qna";
    private static final String TITLE = "\uD83D\uDE4B 질문";
    private final BoardService qnaService;

    private final SyllabusService syllabusService;
    private final LectureService lectureService;
    private final MemberService memberService;
    private final FileService fileService;

    // 전체 질문 리스트
    @GetMapping
    public String readList(Model model, Principal principal, HttpServletRequest request,
                           @Nullable @RequestParam("year-semester") String yearSemester,
                           @Nullable @RequestParam("syllabus-id") String syllabusId) {

        // 교수, 학생 따로 수업하는 또는 수업듣는 강의 모두 가져오기
        Map<String, List<Syllabus>> lectures = request.isUserInRole("ROLE_PROFESSOR") ?
                syllabusService.mapAllByProfessorId(principal.getName()) :
                lectureService.mapAllByStudentId(principal.getName());


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
                    boards.addAll(qnaService.listAll(syllabus.getId())));

            model.addAttribute("selectedSyllabusId", "");
            model.addAttribute("selectedSyllabusName", "전체");
        }
        else {
            boards.addAll(qnaService.listAll(syllabusId));

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
                            (request.isUserInRole("ROLE_PROFESSOR") ? "" : " - " + syllabus.getProfessor().getName())
                            + " (" + syllabus.formatClassTime() + ")");
        }

        // 날짜 내림차순 정렬 후 모델에 넣기
        boards.sort(Comparator.comparing(Board::getDate).reversed());
        model.addAttribute("boards", boards);
        model.addAttribute("rootURL", ROOT_URL);
        model.addAttribute("title", TITLE);

        return "board/list";
    }

    // 열람
    @GetMapping("{boardIdStr:[0-9]+}")
    public String view(Model model, Principal principal, HttpServletRequest request,
                       @PathVariable String boardIdStr) {

        // 게시글 가져오기
        int boardId = Integer.parseInt(boardIdStr);
        Optional<Board> oBoard = qnaService.read(boardId);

        // 없으면 404
        if (oBoard.isEmpty())
            return "error/404";

        // 읽을 권한 없으면 403
        Board board = oBoard.get();
        BiPredicate<String, String> auth = request.isUserInRole("ROLE_PROFESSOR") ?
                syllabusService::existsByIdAndProfessor_Id :
                lectureService::existsBySyllabus_IdAndStudent_Id;
        if (!auth.test(board.getSyllabus().getId(), principal.getName()))
            return "error/403";

        // 조회 수 증가
        qnaService.increaseViewCount(boardId);

        // 열람
        model.addAttribute("rootURL", ROOT_URL);
        model.addAttribute("title", TITLE);
        model.addAttribute("board", board);
        return "board/view";
    }

    // 작성
    @GetMapping("write")
    public String getWriting(Model model, Principal principal, HttpServletRequest request,
                             @Nullable @RequestParam("syllabus-id") String syllabusId) {

        // 학정번호가 넘어왔으면 그걸로 강의 모델에 추가 아니면 교수한 강의 최근 1개 추가
        model.addAttribute("rootURL", ROOT_URL);
        model.addAttribute("title", TITLE);

        Function<String, Optional<Syllabus>> findFirstSyllabus = request.isUserInRole("ROLE_PROFESSOR") ?
                syllabusService::findFirstByProfessor_IdOrderByIdDesc :
                lectureService::findFirstByStudent_IdOrderBySyllabusDesc;
        model.addAttribute("syllabus", syllabusId == null ?
                findFirstSyllabus.apply(principal.getName()).get() :
                syllabusService.findById(syllabusId).get());

        return "/board/write";
    }
    @PostMapping("write")
    public String postWriting(BoardDto boardDto, Principal principal,
                              HttpServletRequest request) throws IOException {

        // 작성 권한 없으면 403
        BiPredicate<String, String> auth = request.isUserInRole("ROLE_PROFESSOR") ?
                syllabusService::existsByIdAndProfessor_Id :
                lectureService::existsBySyllabus_IdAndStudent_Id;
        if (!auth.test(boardDto.getSyllabusId(), principal.getName()))
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
        qnaService.save(board);

        // 작성된 포스트 번호로 뷰 이동
        return String.format("redirect:/%c/%s/%d",
                request.isUserInRole("ROLE_PROFESSOR") ? 'p' : 's',
                ROOT_URL, board.getId());
    }



    // 댓글 작성
    @PostMapping("add-comment")
    @ResponseBody
    public String addComment(String comment, Principal principal, HttpServletRequest request) {
        return "아직 안만듬";
    }

    // 댓글 삭제
    @GetMapping("delete-comment/{commentIdStr:[0-9]+}")
    @ResponseBody
    public String deleteComment(String comment, Principal principal, HttpServletRequest request,
                             @PathVariable String commentIdStr) {
        return "아직 안만듬";
    }
}
