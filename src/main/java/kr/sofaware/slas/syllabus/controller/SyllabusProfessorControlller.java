package kr.sofaware.slas.syllabus.controller;

import kr.sofaware.slas.entity.Syllabus;
import kr.sofaware.slas.service.SyllabusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/p")
@Log4j2
public class SyllabusProfessorControlller {
    private final SyllabusService syllabusService;

    @GetMapping("syllabus")
    public String getProfessorGrade(Authentication authentication, Principal principal, Model model,
                                    @Nullable @RequestParam("year-semester") String yearSemester,
                                    @Nullable @RequestParam("syllabus-id") String syllabusId)
    {
        String professorId = principal.getName();
        List<Syllabus> syllabuses = syllabusService.findAllByProfessorId(professorId);

        model.addAttribute("syllabuses", syllabuses);

        return "syllabus/pSyllabus";
    }

    @GetMapping("syllabusDetail/{syllabus-id}")
    public String getProfessorSyllabusDetail(Authentication authentication, Principal principal, Model model,
                                           @PathVariable("syllabus-id") String syllabusId)
    {
        Optional<Syllabus> syllabusInfo = syllabusService.findBySyllabusId(syllabusId);
        model.addAttribute("syllabusYear", Syllabus.formatYearSemester(syllabusId));
        model.addAttribute("syllabusInfo", syllabusInfo);

        return "syllabus/pSyllabusDetail";
    }

    @PostMapping("syllabusDetail/{syllabus-id}")
    public String postProfessorSyllabusDetail(Authentication authentication, Principal principal, Model model,
                                             @PathVariable("syllabus-id") String syllabusId,
                                              @RequestParam("category") String category,
                                              @RequestParam("credit") int credit,
                                              @RequestParam("dayofweek1") String dayofweek1,
                                              @RequestParam("time1") String time1,
                                              @RequestParam("dayofweek2") String dayofweek2,
                                              @RequestParam("time2") String time2,
                                              @RequestParam("introduction") String introduction)
    {
        Optional<Syllabus> syllabus = syllabusService.findBySyllabusId(syllabusId);

        syllabus.ifPresent( selectSyllabus->{
            selectSyllabus.setCategory(category);
            selectSyllabus.setCredit(credit);
            selectSyllabus.setDayOfWeek1(dayofweek1);
            selectSyllabus.setTime1(time1);
            selectSyllabus.setDayOfWeek2(dayofweek2);
            selectSyllabus.setTime2(time2);
            selectSyllabus.setIntroduction(introduction);
            syllabusService.saveByPost(selectSyllabus);
        });

        String path = String.format("redirect:%s", syllabusId);
        return path;
    }
}
