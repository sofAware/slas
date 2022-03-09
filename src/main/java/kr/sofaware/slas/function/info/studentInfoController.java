package kr.sofaware.slas.function.info;

import kr.sofaware.slas.entity.Attendance;
import kr.sofaware.slas.entity.Syllabus;
import kr.sofaware.slas.service.AttendanceService;
import kr.sofaware.slas.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/s")
public class studentInfoController {
    private final LectureService lectureService; //강의 정보 받아올 곳 (강의 이름, 학정번호)
    private final AttendanceService attendanceService; //출석 체크 결과 받아올 곳


    @GetMapping("/info")
    public String getAttendance(Model model, Authentication authentication, Principal principal,
                                @Nullable @RequestParam("year-semester") String yearSemester,
                                @Nullable @RequestParam("syllabus-id") String syllabusId) {

        Collection<? extends GrantedAuthority> auth = authentication.getAuthorities();
        String Id = principal.getName();

        model.addAttribute("id", Id);
        model.addAttribute("auth", auth);

        Map<String, List<Syllabus>> lectures = lectureService.mapAllByStudentId(principal.getName());

        // 학기, 과목 선택 없는 요청이면 제일 최근 학기와 사전 순 빠른 학정번호를 선택
        if (syllabusId == null) {
            if (yearSemester == null) {
                ArrayList<String> yearSemesters = new ArrayList<>(lectures.keySet());
                // 이 사람이 들었던 수업이 없을 경우 그냥 리턴
                if (yearSemesters.isEmpty())
                    return "notice/sNotice";

                yearSemester = yearSemesters.get(0);
            }
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

        // 게시판 목록 긁어오기
        List<Attendance> attendances = attendanceService.listAll(Id, syllabusId);
        model.addAttribute("attendances", attendances);
        return "info/sInfo";
    }
}