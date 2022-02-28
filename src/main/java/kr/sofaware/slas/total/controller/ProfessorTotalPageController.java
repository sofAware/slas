package kr.sofaware.slas.total.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
@RequestMapping("/p")
public class ProfessorTotalPageController {

    //교수 강의 종합 페이지
    @GetMapping("total")
    public String professorTotal(Model model, @RequestParam("year-semester") @Nullable String yearSemester) {


        return "total/professor-totalpage";
    }
}
