package kr.sofaware.slas.board;

import kr.sofaware.slas.entity.Assignment;
import kr.sofaware.slas.entity.Board;
import kr.sofaware.slas.entity.Syllabus;
import kr.sofaware.slas.service.AssignmentService;
import kr.sofaware.slas.service.AssignmentSubmitService;
import kr.sofaware.slas.service.FileService;
import kr.sofaware.slas.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/s/assignment")
@RequiredArgsConstructor
public class AssignmentStudentController {

    private final AssignmentSubmitService assignmentSubmitService;
    private final AssignmentService assignmentService;
    private final LectureService lectureService;
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

        // 읽을 권한 없으면 403
        String syllabusId = oAssignment.get().getSyllabus().getId();
        if (!lectureService.existsBySyllabus_IdAndStudent_Id(syllabusId, principal.getName()))
            return "error/403";

        // 모델에 속성 추가
        model.addAttribute("assignment", oAssignment.get());
        Optional<Board> oBoard = assignmentSubmitService.findByAssignment_IdAndMember_Id(
                assignmentId, principal.getName());

        return "assignment/sWrite";
    }
}
