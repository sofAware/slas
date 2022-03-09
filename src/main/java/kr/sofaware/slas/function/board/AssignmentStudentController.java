package kr.sofaware.slas.function.board;

import kr.sofaware.slas.entity.Assignment;
import kr.sofaware.slas.entity.Board;
import kr.sofaware.slas.entity.Syllabus;
import kr.sofaware.slas.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/s/assignment")
@RequiredArgsConstructor
public class AssignmentStudentController {

    private final AssignmentSubmitService assignmentSubmitService;
    private final AssignmentService assignmentService;
    private final LectureService lectureService;
    private final MemberService memberService;
    private final FileService fileService;

    // 리스트
    @GetMapping
    public String readList(Model model, Principal principal,
                           @Nullable @RequestParam("year-semester") String yearSemester,
                           @Nullable @RequestParam("syllabus-id") String syllabusId) {


        // 사용자의 수강 학기와 과목들 조회
        Map<String, List<Syllabus>> lectures = lectureService.mapAllByStudentId(principal.getName());

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

        // 강의 선택 없으면 해당 학기 전체 과제 긁어오기
        List<Assignment> assignments = new ArrayList<>();
        if (syllabusId == null || syllabusId.isEmpty()) {
            lectures.get(yearSemester).forEach(syllabus ->
                    assignments.addAll(assignmentService.listAll(syllabus.getId())));

            // 템플릿에서 강의명과 강의시간을 표시하기 위해 (isEmpty 판별) 추가
            model.addAttribute("selectedSyllabusId", "");
            model.addAttribute("selectedSyllabusName", "전체");
        } else {
            assignments.addAll(assignmentService.listAll(syllabusId));

            // 선택된 강의 lectures에서 찾아서 강의명 입력
            Syllabus syllabus = lectures
                    .get(yearSemester)
                    .stream()
                    .filter(s -> s.getId().equals(syllabusId))
                    .findAny()
                    .get();
            model.addAttribute("selectedSyllabusId", syllabus.getId());
            model.addAttribute("selectedSyllabusName",
                    syllabus.getName() + " (" + syllabus.formatClassTime() + ")");
        }

        // 제출기한 시작일 내림차순 정렬 후 모델에 넣기
        assignments.sort(Comparator.comparing(Assignment::getSubmitStart).reversed());
        model.addAttribute("assignments", assignments);
        return "assignment/list";
    }

    // 열람
    @GetMapping("{assignmentIdStr:[0-9]+}")
    public String view(Model model, Principal principal,
                       @PathVariable String assignmentIdStr) {

        // 과제 가져오기
        int assignmentId = Integer.parseInt(assignmentIdStr);
        Optional<Assignment> oAssignment = assignmentService.read(assignmentId);

        // 과제가 없으면 404
        if (oAssignment.isEmpty())
            return "error/404";

        // 읽을 권한 없으면 403
        String syllabusId = oAssignment.get().getSyllabus().getId();
        if (!lectureService.existsBySyllabus_IdAndStudent_Id(syllabusId, principal.getName()))
            return "error/403";

        // 모델에 속성 추가
        model.addAttribute("assignment", oAssignment.get());
        Optional<Board> oBoard = assignmentSubmitService.findByAssignment_IdAndMember_Id(
                assignmentId, principal.getName());

        // 내 제출이 없을 경우 작성으로 리디렉션
        if (oBoard.isEmpty())
            return "redirect:/s/assignment/write/" + assignmentId;

        // 열람
        model.addAttribute("board", oBoard.get());
        return "assignment/sView";
    }

    // 작성
    @GetMapping("write/{assignmentIdStr:[0-9]+}")
    public String getWriting(Model model, Principal principal,
                             @PathVariable String assignmentIdStr) {

        // 과제 가져오기
        int assignmentId = Integer.parseInt(assignmentIdStr);
        Optional<Assignment> oAssignment = assignmentService.read(assignmentId);

        // 과제가 없으면 404
        if (oAssignment.isEmpty())
            return "error/404";

        // 과제가 등록된 강의를 듣지 않는다면 403
        String syllabusId = oAssignment.get().getSyllabus().getId();
        if (!lectureService.existsBySyllabus_IdAndStudent_Id(syllabusId, principal.getName()))
            return "error/403";

        // 모델에 속성 추가
        model.addAttribute("assignment", oAssignment.get());
        Optional<Board> oBoard = assignmentSubmitService.findByAssignment_IdAndMember_Id(
                assignmentId, principal.getName());

        return "assignment/sWrite";
    }

    @PostMapping("write/{assignmentIdStr:[0-9]+}")
    public String postWriting(BoardDto boardDto, Principal principal,
                              @PathVariable String assignmentIdStr) throws IOException {

        // 과제 가져오기
        int assignmentId = Integer.parseInt(assignmentIdStr);
        Optional<Assignment> oAssignment = assignmentService.read(assignmentId);

        // 과제가 없으면 404
        if (oAssignment.isEmpty())
            return "error/404";

        // 과제가 등록된 강의를 듣지 않는다면 403
        Assignment assignment = oAssignment.get();
        Syllabus syllabus = assignment.getSyllabus();
        if (!lectureService.existsBySyllabus_IdAndStudent_Id(syllabus.getId(), principal.getName()))
            return "error/403";

        // 첨부파일 저장
        String attachmentPath = "";
        if (!boardDto.getFile().isEmpty())
            attachmentPath = fileService.saveOnSyllabus(boardDto.getFile(), boardDto.getSyllabusId());

        // 과제 제출 엔티티 (Board) 만들기
        Board.BoardBuilder builder = Board.builder()
                .syllabus(syllabus)
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .member(memberService.loadUserByUsername(principal.getName()))
                .view(0)
                .assignment(assignment);

        // 첨부파일이 있을 경우 보드에 추가
        if (!boardDto.getFile().isEmpty())
            builder.attachmentName(boardDto.getFile().getOriginalFilename())
                    .attachmentPath(attachmentPath);

        // 과제 제출
        Board board = builder.build();
        assignmentSubmitService.save(board);

        // 과제로 리디렉션
        return "redirect:/s/assignment/" + assignment.getId();
    }

    // 수정
    @GetMapping("edit/{assignmentIdStr:[0-9]+}")
    public String getEditing(Model model, Principal principal,
                       @PathVariable String assignmentIdStr) {

        // 과제 가져오기
        int assignmentId = Integer.parseInt(assignmentIdStr);
        Optional<Assignment> oAssignment = assignmentService.read(assignmentId);

        // 과제가 없으면 404
        if (oAssignment.isEmpty())
            return "error/404";

        // 제출한 과제 가져오기
        Optional<Board> oBoard = assignmentSubmitService.findByAssignment_IdAndMember_Id(assignmentId, principal.getName());

        // 제출한 과제가 없으면 404
        if (oBoard.isEmpty())
            return "error/404";

        // 과제가 등록된 강의를 듣지 않는다면 403
        String syllabusId = oAssignment.get().getSyllabus().getId();
        if (!lectureService.existsBySyllabus_IdAndStudent_Id(syllabusId, principal.getName()))
            return "error/403";

        // 페이지 전송
        model.addAttribute("assignment", oAssignment.get());
        model.addAttribute("board", oBoard.get());
        return "assignment/sWrite";
    }

    @PostMapping("edit/{assignmentIdStr:[0-9]+}")
    public String postEditing(BoardDto boardDto, Principal principal,
                              @PathVariable String assignmentIdStr) throws IOException {

        // 과제 가져오기
        int assignmentId = Integer.parseInt(assignmentIdStr);
        Optional<Assignment> oAssignment = assignmentService.read(assignmentId);

        // 과제가 없으면 404
        if (oAssignment.isEmpty())
            return "error/404";

        // 과제가 등록된 강의를 듣지 않는다면 403
        Assignment assignment = oAssignment.get();
        if (!lectureService.existsBySyllabus_IdAndStudent_Id(assignment.getSyllabus().getId(), principal.getName()))
            return "error/403";

        // 제출한 과제 가져오기
        Optional<Board> oBoard = assignmentSubmitService.findByAssignment_IdAndMember_Id(assignmentId, principal.getName());

        // 제출한 과제가 없으면 404
        if (oBoard.isEmpty())
            return "error/404";

        // 첨부파일 가져오기
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

        // 새로운 값들로 세팅 후 과제 다시 제출
        board.update(boardDto.getTitle(), boardDto.getContent(), attachmentName, attachmentPath);
        assignmentSubmitService.save(board);

        // 과제로 리디렉션
        return "redirect:/s/assignment/" + assignment.getId();
    }

    // 삭제
    @GetMapping("delete/{assignmentIdStr:[0-9]+}")
    public String delete(Principal principal,
                         @PathVariable String assignmentIdStr) {

        // 과제 가져오기
        int assignmentId = Integer.parseInt(assignmentIdStr);
        Optional<Assignment> oAssignment = assignmentService.read(assignmentId);

        // 없으면 404
        if (oAssignment.isEmpty())
            return "error/404";

        // 과제가 등록된 강의를 듣지 않는다면 403
        Syllabus syllabus = oAssignment.get().getSyllabus();
        if (!lectureService.existsBySyllabus_IdAndStudent_Id(syllabus.getId(), principal.getName()))
            return "error/403";

        // 제출한 과제 가져오기
        Optional<Board> oBoard = assignmentSubmitService.findByAssignment_IdAndMember_Id(assignmentId, principal.getName());

        // 제출한 과제가 없으면 404
        if (oBoard.isEmpty())
            return "error/404";

        // 파일 및 제출한 과제 삭제
        fileService.deleteOnSyllabus(oBoard.get().getAttachmentPath());
        assignmentSubmitService.delete(oBoard.get().getId());

        // 목록으로 리디렉션
        return "redirect:/s/assignment/" + assignmentId;
    }
}
