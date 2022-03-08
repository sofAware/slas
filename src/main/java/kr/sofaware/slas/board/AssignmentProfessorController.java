package kr.sofaware.slas.board;

import kr.sofaware.slas.entity.Assignment;
import kr.sofaware.slas.entity.Board;
import kr.sofaware.slas.entity.LectureVideo;
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
@RequestMapping("/p/assignment")
@RequiredArgsConstructor
public class AssignmentProfessorController {

    private final AssignmentSubmitService assignmentSubmitService;
    private final AssignmentService assignmentService;
    private final SyllabusService syllabusService;
    private final FileService fileService;

    // 리스트
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

        // 없으면 404
        if (oAssignment.isEmpty())
            return "error/404";

        // 읽을 권한 없으면 403
        String syllabusId = oAssignment.get().getSyllabus().getId();
        if (!syllabusService.existsByIdAndProfessor_Id(syllabusId, principal.getName()))
            return "error/403";

        // 열람
        model.addAttribute("assignment", oAssignment.get());
        model.addAttribute("infos", assignmentSubmitService.listSubmitInfo(syllabusId));
        return "assignment/pView";
    }

    // 작성
    @GetMapping("write")
    public String getWriting(Model model, Principal principal,
                             @Nullable @RequestParam("syllabus-id") String syllabusId) {

        // 학정번호가 넘어왔으면 그걸로 강의 아니면 교수한 강의 최근 1개
        Optional<Syllabus> syllabus = syllabusId == null ?
                syllabusService.findFirstByProfessor_IdOrderByIdDesc(principal.getName()) :
                syllabusService.findById(syllabusId);

        // 해당 강의가 없다면 잘못된 요청
        if (syllabus.isEmpty())
            return "error/400";

        // 작성
        model.addAttribute("syllabus", syllabus.get());
        return "assignment/pWrite";
    }

    @PostMapping("write")
    public String postWriting(AssignmentDto assignmentDto, Principal principal) throws Exception {

        // 작성 권한 없으면 403
        if (!syllabusService.existsByIdAndProfessor_Id(
                assignmentDto.getSyllabusId(),
                principal.getName()))
            return "error/403";

        // 첨부파일 저장
        String attachmentPath = "";
        if (!assignmentDto.getFile().isEmpty())
            attachmentPath = fileService.saveOnSyllabus(assignmentDto.getFile(), assignmentDto.getSyllabusId());

        // 과제 엔티티 만들기
        Assignment.AssignmentBuilder builder = Assignment.builder()
                .syllabus(syllabusService.findById(assignmentDto.getSyllabusId()).get())
                .name(assignmentDto.getTitle())
                .content(assignmentDto.getContent())
                .submitStart(assignmentDto.getSubmitStart())
                .submitEnd(assignmentDto.getSubmitEnd());

        // 파일이 있을경우 파일 정보도 빌더에 추가
        if (assignmentDto.getFile() != null)
            builder.attachmentName(assignmentDto.getFile().getOriginalFilename())
                    .attachmentPath(attachmentPath);

        // 과제 작성
        Assignment assignment = builder.build();
        assignmentService.save(assignment);

        // 작성된 과제로 리디렉션
        return "redirect:/p/assignment/" + assignment.getId();
    }

    // 수정
    @GetMapping("edit/{assignmentIdStr:[0-9]+}")
    public String getEditing(Model model, Principal principal,
                             @PathVariable String assignmentIdStr) {

        // 과제 가져오기
        int assignmentId = Integer.parseInt(assignmentIdStr);
        Optional<Assignment> assignment = assignmentService.read(assignmentId);

        // 없으면 404
        if (assignment.isEmpty())
            return "error/404";

        // 글 작성자가 아니면 403
        if (!syllabusService.existsByIdAndProfessor_Id(
                assignment.get().getSyllabus().getId(),
                principal.getName()))
            return "error/403";

        // 페이지 전송
        model.addAttribute("syllabus", assignment.get().getSyllabus());
        model.addAttribute("assignment", assignment.get());
        return "/assignment/pWrite";
    }
    @PostMapping("edit/{assignmentIdStr:[0-9]+}")
    public String postEditing(AssignmentDto assignmentDto, Principal principal,
                              @PathVariable String assignmentIdStr) throws IOException {

        // 과제 가져오기
        int assignmentId = Integer.parseInt(assignmentIdStr);
        Optional<Assignment> oAssignment = assignmentService.read(assignmentId);

        // 없으면 404
        if (oAssignment.isEmpty())
            return "error/404";

        // 글 작성자가 아니면 403
        Assignment assignment = oAssignment.get();
        if (!syllabusService.existsByIdAndProfessor_Id(
                oAssignment.get().getSyllabus().getId(),
                principal.getName()))
            return "error/403";

        // 첨부파일 가져오기
        String attachmentName = assignment.getAttachmentName();
        String attachmentPath = assignment.getAttachmentPath();

        // 새로운 파일을 업로드하면
        if (!assignmentDto.getFile().isEmpty()) {
            if (attachmentName != null && !attachmentName.isEmpty())
                // 기존 파일 삭제
                fileService.deleteOnSyllabus(attachmentPath);
            attachmentName = assignmentDto.getFile().getOriginalFilename();
            attachmentPath = fileService.saveOnSyllabus(assignmentDto.getFile(), assignmentDto.getSyllabusId());
        }
        // 새로운 파일을 업로드 하지는 않았지만 게시글에 파일이 존재하고 파일 삭제를 원했다면 삭제!
        else if (attachmentName != null && !attachmentName.isEmpty() &&
                assignmentDto.getDeleteFile() != null && !assignmentDto.getDeleteFile().isEmpty()) {
            fileService.deleteOnSyllabus(attachmentPath);
            attachmentName = "";
            attachmentPath = "";
        }

        // 새로운 값들로 세팅
        assignment.update(
                assignmentDto.getTitle(),
                assignmentDto.getSubmitStart(),
                assignmentDto.getSubmitEnd(),
                assignmentDto.getContent(),
                attachmentName, attachmentPath);
        assignmentService.save(assignment);

        // 수정된 포스트 번호로 뷰 이동
        return "redirect:/p/assignment/" + assignmentIdStr;
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

        // 글 작성자가 아니면 403
        String syllabusId = oAssignment.get().getSyllabus().getId();
        if (!syllabusService.existsByIdAndProfessor_Id(
                syllabusId,
                principal.getName()))
            return "error/403";

        // 학생들이 제출한 과제 전부 삭제
        List<AssignmentSubmitInfo> infos = assignmentSubmitService
                .listSubmitInfo(syllabusId);
        infos.forEach(info ->
            // 제출정보에 게시글이 있을 경우 삭제
            info.getBoard().ifPresent(board -> {
                // 게시글에 첨부파일이 있을 경우 삭제
                if (!board.getAttachmentName().isEmpty())
                    fileService.deleteOnSyllabus(syllabusId);
                // 게시글 삭제
                assignmentSubmitService.delete(board.getId());
            }));

        // 파일 및 게시글 삭제
        fileService.deleteOnSyllabus(oAssignment.get().getAttachmentPath());
        assignmentService.delete(assignmentId);

        // 목록으로 리디렉션
        return "redirect:/p/assignment";
    }
}
