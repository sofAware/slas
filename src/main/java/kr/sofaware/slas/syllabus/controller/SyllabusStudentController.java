package kr.sofaware.slas.syllabus.controller;

import kr.sofaware.slas.entity.Syllabus;
import kr.sofaware.slas.service.SyllabusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/s")
@Log4j2
public class SyllabusStudentController {

    private final SyllabusService syllabusService;

    @GetMapping("syllabus")
    public String getStudentSyllabus(Authentication authentication, Principal principal, Model model,
                                     @Nullable @RequestParam("syllabus-name") String syllabusName,
                                     @Nullable @RequestParam("professor-name") String professorName)
    {
        List<Syllabus> syllabuses = null;
        if(syllabusName != null || professorName != null)
        {
            log.error("if is success");
            syllabuses = syllabusService.findAllBySyllabusNameOrProfessorName(syllabusName, professorName);
            model.addAttribute("syllabuses", syllabuses);
        }

        return "syllabus/sSyllabus";
    }
}
