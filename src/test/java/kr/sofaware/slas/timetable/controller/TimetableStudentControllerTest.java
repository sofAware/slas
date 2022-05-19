//package kr.sofaware.slas.timetable.controller;
//
//import kr.sofaware.slas.entity.Syllabus;
//import kr.sofaware.slas.service.LectureService;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.lang.Nullable;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.security.Principal;
//import java.util.*;
//
//@SpringBootTest
//class TimetableStudentControllerTest {
//
//    private final LectureService lectureService;
//
//    TimetableStudentControllerTest(LectureService lectureService) {
//        this.lectureService = lectureService;
//    }
//
//    @Test
//    @GetMapping("timetable")
//    public String getStudentTimetable(Authentication authentication, Principal principal, Model model,
//                                      @Nullable @RequestParam("year-semester") String yearSemester) {
//
//        Collection<? extends GrantedAuthority> auth = authentication.getAuthorities();
//        String studentId = principal.getName();
//
//        model.addAttribute("id", studentId);
//        model.addAttribute("auth", auth);
//
//        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//
//        //lecture service
//        Map<String, List<Syllabus>> lectures = lectureService.mapAllByStudentId(studentId);
//
//        //학기 선택이 없는 요청이면 제일 최근 학기 시간표
//        if (yearSemester == null) {
//            ArrayList<String> yearSemesters = new ArrayList<>(lectures.keySet()); //모든 key 값
//            yearSemester = yearSemesters.get(0); //reverseOrder이기 때문에 get(0)이 가장 최근 학기
//        }
//
//        //학기 선택 리스트
//        Map<String, String> formatYS = new TreeMap<>(Collections.reverseOrder());
//        lectures.keySet().forEach(s -> formatYS.put(s, Syllabus.formatYearSemester(s)));
//        model.addAttribute("mapYS", formatYS);
//        model.addAttribute("yearSemester", Syllabus.formatYearSemester(yearSemester));
//
//        //시간표 정보 보내기
//        /*
//            월화수목금토 배열에
//
//         */
//
//
//        //교시 배열 - 9교시까지
//        ArrayList<ArrayList<Syllabus>> timetableInfo = new ArrayList<ArrayList<Syllabus>>(9);
//
//        //월화수목금토 배열
//        ArrayList<Syllabus> timetableByDay;
//
//        //초기화 작업 후 2차원 배열에 대입
//        for (int i = 0; i < 9; i++) {
//            timetableByDay = new ArrayList<>(7);
//            for (int j = 0; j < 7; j++) {
//                if (j == 0) timetableByDay.add(null);
//                timetableByDay.add(new Syllabus());
//            }
//            timetableInfo.add(timetableByDay);
//        }
//
//        //현재 학기의 학생의 시간표 가져오기
//        lectures.get(yearSemester).forEach(syllabus -> {
//            timetableInfo.get(Integer.parseInt(syllabus.getTime1()) - 1)
//                    .add(Syllabus.formatDayToInt(syllabus.getDayOfWeek1()), syllabus);
//        });
//        model.addAttribute("timetableInfo", timetableInfo);
//
//        timetableInfo.forEach(syllabi -> {
//            syllabi.forEach(syllabus -> {
//                System.out.println(syllabus.getName());
//                System.out.println(syllabus.getId());
//            });
//            System.out.println();
//        });
//
//        return "OG_sTimetable";
//    }
//}