package kr.sofaware.slas.function.info;

import kr.sofaware.slas.entity.Attendance;
import kr.sofaware.slas.entity.Syllabus;
import kr.sofaware.slas.service.AttendanceService;
import kr.sofaware.slas.service.SyllabusService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.*;
@RequiredArgsConstructor
@Controller
@RequestMapping("/p")
public class studentInfoProfessorController {

    private final SyllabusService syllabusService; //강의 정보 가져올 때 사용
    private final AttendanceService attendanceService;

    @GetMapping("/info")
    public String getProfessorGrade(Authentication authentication, Principal principal, Model model,
                                    @Nullable @RequestParam("year-semester") String yearSemester,
                                    @Nullable @RequestParam("syllabus-id") String syllabusId)
    {
        String professorId = principal.getName();
        // 사용자의 수강 학기와 과목들 조회
        Map<String, List<Syllabus>> lectures = syllabusService.mapAllByProfessorId(professorId);

        // 학기, 과목 선택 없는 요청이면 제일 최근 학기와 사전 순 빠른 학정번호를 선택
        if (syllabusId == null) {
            if (yearSemester == null) {
                ArrayList<String> yearSemesters = new ArrayList<>(lectures.keySet());
                // 이 사람이 들었던 수업이 없을 경우 그냥 리턴
                if (yearSemesters.isEmpty())
                    return "grade/pGradeMain";

                yearSemester = yearSemesters.get(0);
            }

            // 첫번째 과목
            syllabusId = lectures.get(yearSemester).get(0).getId();
        } else if (yearSemester == null) {
            yearSemester = syllabusId.substring(0, 4);
        }

        Map<String, String> formatYS = new TreeMap<>(Collections.reverseOrder());
        lectures.keySet().forEach(s -> formatYS.put(s, Syllabus.formatYearSemester(s)));

        // 학기 선택 리스트
        model.addAttribute("mapYS", formatYS);
        model.addAttribute("yearSemester", Syllabus.formatYearSemester(yearSemester));

        // 강의 선택 리스트
        model.addAttribute("syllabuses", lectures.get(yearSemester));
        String finalSyllabusId = syllabusId;
        model.addAttribute("selectedSyllabusName",
                lectures.get(yearSemester).stream().filter(s -> s.getId().equals(finalSyllabusId))
                        .findAny().get().getName());

        List<Attendance> attendances = attendanceService.listAll(syllabusId);
        model.addAttribute("attendances", attendances);
//        for(int i=0;i< attendances.size();i++){
//            List<String> semester= attendances.get(i).getStudent().getSemester();
//        }


        return "info/pStudentInfo";
    }
}
