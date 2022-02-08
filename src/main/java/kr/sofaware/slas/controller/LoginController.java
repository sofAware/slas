package kr.sofaware.slas.controller;

import kr.sofaware.slas.mainpage.dto.ProfessorDto;
import kr.sofaware.slas.mainpage.dto.StudentDto;
import kr.sofaware.slas.service.ProfessorService;
import kr.sofaware.slas.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Controller
public class LoginController {
    @Autowired
    private final StudentService studentService;

    @Autowired
    private final ProfessorService professorService;

    //회원가입 GET
    @GetMapping("/signup")
    public String getSignup() {
        return "signup";
    }

    //회원가입 POST 요청
    @PostMapping("/signup")
    public String postSignup(@RequestParam("auth") String auth, StudentDto studentDto, ProfessorDto professorDto) {

        if(auth.equals("ROLE_STUDENT"))
            studentService.save(studentDto);
        else
            professorService.save(professorDto);

        return "redirect:/login";
    }

    //로그인
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/logout") // logout by GET 요청
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder
                .getContext().getAuthentication());
        return "redirect:/login";
    }
}
