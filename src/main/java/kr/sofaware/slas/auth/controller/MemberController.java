package kr.sofaware.slas.auth.controller;

import kr.sofaware.slas.auth.dto.MemberDTO;
import kr.sofaware.slas.auth.dto.AuthDTO;
import kr.sofaware.slas.auth.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 로그인 화면
    @GetMapping("/login")
    public String login() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth instanceof AnonymousAuthenticationToken ? "auth/login" : "redirect:/";
    }

    // 회원가입 페이지 요청시 로그인 페이지로 리디렉션
    @GetMapping("/signup")
    public String getSignup() {
        return "redirect:/login#signup";
    }

    // 회원가입 요청
    @PostMapping("/signup")
    @ResponseBody
    public AuthDTO postSignup(@RequestBody MemberDTO memberDTO) {
        if (memberService.isUserExist(memberDTO.getId())) {
            return new AuthDTO(false, "이미 등록된 학번 입니다.<br>" + memberDTO.getId());
        } else {
            memberService.save(memberDTO);
            return new AuthDTO(true, "");
        }
    }

    // 로그아웃 요청
    @GetMapping("/logout") // logout by GET 요청
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder
                .getContext().getAuthentication());
        return "redirect:/login";
    }
}
