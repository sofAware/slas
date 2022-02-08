package kr.sofaware.slas.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/p")
public class TimetableProfessorController {

    @GetMapping("timetable")
    public String getStudentTimetable(Authentication authentication, Principal principal, Model model){

        Collection<? extends GrantedAuthority> auth = authentication.getAuthorities();
        String userId = principal.getName();

        model.addAttribute("id", userId);
        model.addAttribute("auth", auth);


        return "timetable";
    }
}