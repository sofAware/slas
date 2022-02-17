package kr.sofaware.slas.mainpage.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class ProfessorMainPageController {
    //교수 메인페이지
    @GetMapping("/professor")
    public String professor() { return "professor"; }
}
