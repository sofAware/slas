package kr.sofaware.slas.timetable.controller;

import kr.sofaware.slas.entity.Syllabus;
import kr.sofaware.slas.service.SyllabusService;
import kr.sofaware.slas.timetable.dto.timetableDTO;
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
@RequestMapping("/p")
public class TimetableProfessorController {

    private final SyllabusService syllabusService;

    @GetMapping("timetable")
    public String getStudentTimetable(Authentication authentication, Principal principal, Model model,
                                      @Nullable @RequestParam("year-semester") String yearSemester) {

        Collection<? extends GrantedAuthority> auth = authentication.getAuthorities();
        String professorId = principal.getName();

        model.addAttribute("id", professorId);
        model.addAttribute("auth", auth);

        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

        //lecture service
        Map<String, List<Syllabus>> lectures = syllabusService.mapAllByProfessorId(professorId);

        //학기 선택이 없는 요청이면 제일 최근 학기 시간표
        if(yearSemester == null) {
            ArrayList<String> yearSemesters = new ArrayList<>(lectures.keySet()); //모든 key 값
            yearSemester = yearSemesters.get(0); //reverseOrder이기 때문에 get(0)이 가장 최근 학기
        }

        //학기 선택 리스트
        Map<String, String> formatYS = new TreeMap<>(Collections.reverseOrder());
        lectures.keySet().forEach(s -> formatYS.put(s, Syllabus.formatYearSemester(s)));
        model.addAttribute("mapYS", formatYS);
        model.addAttribute("yearSemester", Syllabus.formatYearSemester(yearSemester));

        //시간표 정보 보내기

        //교시 배열 - 9교시까지
        ArrayList<ArrayList<timetableDTO>> timetableInfo = new ArrayList<ArrayList<timetableDTO>>(9);

        //월화수목금토 배열
        ArrayList<timetableDTO> timetableByDay;

        for(int i = 0; i < 9; i++)
        {
            timetableByDay = new ArrayList<timetableDTO>(6);
            for(int j = 0; j < 6 ; j++)
            {
                timetableByDay.add(new timetableDTO());
            }
            timetableInfo.add(timetableByDay);
        }

        //log.error("timetableInfo size : " + timetableInfo.size());
        //log.error("timetableInfo get size : " + timetableInfo.get(0).size());

        //현재 학기의 학생의 시간표 가져오기
        lectures.get(yearSemester).forEach(syllabus -> {
            timetableInfo.get(Integer.parseInt(syllabus.getTime1()) - 1)
                    .add(Syllabus.formatDayToInt(syllabus.getDayOfWeek1()), new timetableDTO(syllabus.getName(), syllabus.getProfessor().getName()));
            timetableInfo.get(Integer.parseInt(syllabus.getTime1()) - 1)
                    .remove(Syllabus.formatDayToInt(syllabus.getDayOfWeek1()) + 1);

            timetableInfo.get(Integer.parseInt(syllabus.getTime2()) - 1)
                    .add(Syllabus.formatDayToInt(syllabus.getDayOfWeek2()), new timetableDTO(syllabus.getName(), syllabus.getProfessor().getName()));
            timetableInfo.get(Integer.parseInt(syllabus.getTime2()) - 1)
                    .remove(Syllabus.formatDayToInt(syllabus.getDayOfWeek2()) + 1);
        });

        //log.error("timetableInfo size : " + timetableInfo.size());
        //log.error("timetableInfo get size : " + timetableInfo.get(0).size());

        //2차원 배열로 보내면 thymeleaf 오류 떠서 하나씩 보냄
        model.addAttribute("t1", timetableInfo.get(0));
        model.addAttribute("t2", timetableInfo.get(1));
        model.addAttribute("t3", timetableInfo.get(2));
        model.addAttribute("t4", timetableInfo.get(3));
        model.addAttribute("t5", timetableInfo.get(4));
        model.addAttribute("t6", timetableInfo.get(5));

        for(timetableDTO s : timetableInfo.get(6))
        {
            if(s.getClassName() != "")
            {
                model.addAttribute("t7", timetableInfo.get(6));
                break;
            }
        }
        for(timetableDTO s : timetableInfo.get(7))
        {
            if(s.getClassName() != "")
            {
                model.addAttribute("t8", timetableInfo.get(7));
                break;
            }
        }
        for(timetableDTO s : timetableInfo.get(8))
        {
            if(s.getClassName() != "")
            {
                model.addAttribute("t9", timetableInfo.get(8));
                break;
            }
        }

        //model.addAttribute("t7", timetableInfo.get(6));
        //model.addAttribute("t8", timetableInfo.get(7));
        //model.addAttribute("t9", timetableInfo.get(8));

        return "timetable/pTimetable";
    }
}