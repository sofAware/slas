package kr.sofaware.slas.function.attendance;

import kr.sofaware.slas.entity.Assignment;
import kr.sofaware.slas.entity.Attendance;
import kr.sofaware.slas.entity.Syllabus;
import kr.sofaware.slas.function.board.AssignmentDto;
import kr.sofaware.slas.service.AttendanceService;
import kr.sofaware.slas.service.MemberService;
import kr.sofaware.slas.service.SyllabusService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/p")
public class attendanceProfessorController {
    private final MemberService memberService;
    private final AttendanceService attendanceService;
    private final SyllabusService syllabusService;

    @GetMapping("/attendance")
    public String getAttendance(Model model, Authentication authentication, Principal principal,
                                @Nullable @RequestParam("year-semester") String yearSemester,
                                @Nullable @RequestParam("syllabus-id") String syllabusId,
                                @Nullable @RequestParam("week-list") String week) {
        Collection<? extends GrantedAuthority> auth = authentication.getAuthorities();
        String Id = principal.getName();

        model.addAttribute("id", Id);
        model.addAttribute("auth", auth);

        Map<String, List<Syllabus>> lectures = syllabusService.mapAllByProfessorId(principal.getName());

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

        // 학기 선택
        model.addAttribute("mapYS", formatYS);
        model.addAttribute("yearSemester", Syllabus.formatYearSemester(yearSemester));

        // 강의 선택
        model.addAttribute("syllabuses", lectures.get(yearSemester));
        String finalSyllabusId = syllabusId;
        model.addAttribute("selectedSyllabusName",
                lectures.get(yearSemester).stream().filter(s -> s.getId().equals(finalSyllabusId))
                        .findAny().get().getName());

        //주차 선택
        Map<String,Integer>weekList=new HashMap<String,Integer>();
        weekList.put("week1",1);
        weekList.put("week2",2);
        weekList.put("week3",3);
        model.addAttribute("week",weekList);


        List<Attendance> attendances = attendanceService.listAll(syllabusId);
        model.addAttribute("attendances", attendances);
        return "Attendance/pAttendanceGet";
        //return "Attendance/pAttendancePut";
    }

//    @GetMapping("/attendance/{attendanceIdStr:[0-9]+}")
//    public String view(Model model, Authentication authentication, Principal principal,
//                       @PathVariable String atttendanceWeekStr,
//                       @Nullable @RequestParam("year-semester") String yearSemester,
//                       @Nullable @RequestParam("syllabus-id") String syllabusId) {
//
//        Collection<? extends GrantedAuthority> auth = authentication.getAuthorities();
//        String Id = principal.getName();
//
//        model.addAttribute("id", Id);
//        model.addAttribute("auth", auth);
//
//        Map<String, List<Syllabus>> lectures = syllabusService.mapAllByProfessorId(principal.getName());
//
//        if (syllabusId == null) {
//            if (yearSemester == null) {
//                ArrayList<String> yearSemesters = new ArrayList<>(lectures.keySet());
//                // 이 사람이 들었던 수업이 없을 경우 그냥 리턴
//                if (yearSemesters.isEmpty())
//                    return "notice/sNotice";
//
//                yearSemester = yearSemesters.get(0);
//            }
//            syllabusId = lectures.get(yearSemester).get(0).getId();
//        } else if (yearSemester == null) {
//            yearSemester = syllabusId.substring(0, 4);
//        }
//
//
//
//
//
//        Map<String, String> formatYS = new TreeMap<>(Collections.reverseOrder());
//        lectures.keySet().forEach(s -> formatYS.put(s, Syllabus.formatYearSemester(s)));
//
//        // 학기 선택
//        model.addAttribute("mapYS", formatYS);
//        model.addAttribute("yearSemester", Syllabus.formatYearSemester(yearSemester));
//
//        // 강의 선택
//        model.addAttribute("syllabuses", lectures.get(yearSemester));
//        String finalSyllabusId = syllabusId;
//        model.addAttribute("selectedSyllabusName",
//                lectures.get(yearSemester).stream().filter(s -> s.getId().equals(finalSyllabusId))
//                        .findAny().get().getName());
//
//        List<Attendance> attendances = attendanceService.listAll(syllabusId);
//        model.addAttribute("attendances", attendances);
//        // 과제 가져오기
//        //int attendanceWeek = Integer.parseInt(atttendanceWeekStr);
//        //List<Attendance> attendances = attendanceService.read(syllabusId);
//
//        // 열람
//       // model.addAttribute("assignmentWeek", attendances.get().getWeeksByList().get(attendanceWeek));
//        //model.addAttribute("infos", assignmentSubmitService.listSubmitInfo(syllabusId));
//        return "Attendance/pAttendancePut";
//    }


    @GetMapping("/attendance/{attendanceWeekStr:[0-9]+}")
    public String putAttendance(Model model, Authentication authentication, Principal principal,
                                @PathVariable String attendanceWeekStr,
                                @Nullable @RequestParam("year-semester") String yearSemester,
                                @Nullable @RequestParam("syllabus-id") String syllabusId) {
        Collection<? extends GrantedAuthority> auth = authentication.getAuthorities();
        String Id = principal.getName();

        model.addAttribute("id", Id);
        model.addAttribute("auth", auth);

        Map<String, List<Syllabus>> lectures = syllabusService.mapAllByProfessorId(principal.getName());

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

        // 학기 선택
        model.addAttribute("mapYS", formatYS);
        model.addAttribute("yearSemester", Syllabus.formatYearSemester(yearSemester));

        // 강의 선택
        model.addAttribute("syllabuses", lectures.get(yearSemester));
        String finalSyllabusId = syllabusId;
        model.addAttribute("selectedSyllabusName",
                lectures.get(yearSemester).stream().filter(s -> s.getId().equals(finalSyllabusId))
                        .findAny().get().getName());

        //주차 선택
        Map<String,Integer> weekList=new HashMap<String,Integer>();
        weekList.put("week1",1);
        weekList.put("week2",2);
        weekList.put("week3",3);
        model.addAttribute("week",weekList);
        model.addAttribute("attendanceWeekStr",attendanceWeekStr);

        List<Attendance> attendances = attendanceService.listAll(syllabusId);
        model.addAttribute("attendances", attendances);
        return "Attendance/pAttendancePut";
    }


    @PostMapping("/attendance/{attendanceWeekStr:[0-9]+}")
    public String postAttendance(Model model, Authentication authentication, Principal principal,
                                @PathVariable String attendanceWeekStr,
                                @Nullable @RequestParam("year-semester") String yearSemester,
                                @Nullable @RequestParam("syllabus-id") String syllabusId,
                                 AttendanceDto attendanceDto){

//        int attendanceWeek = Integer.parseInt(attendanceWeekStr);
//        List<Attendance> attendances = attendanceService.listAll(syllabusId);
//        Optional<Attendance> attendanceOptional=attendanceService.update(syllabusId,attendanceWeek)
//        model.addAttribute("attendances",attendances);
//        model.addAttribute("attendanceWeek",attendanceWeek);
//
//        attendanceOptional.isPresent(selectAttendance ->  selectAttendance.setName());

//        Attendance.AttendanceBuilder builder =Attendance.builder()
//                .syllabus(syllabusService.findById(attendanceDto.getSyllabus()).get())
//
        return "redirect:/p/attendance/" + attendanceWeekStr;
    }
}