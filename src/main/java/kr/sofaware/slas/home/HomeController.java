package kr.sofaware.slas.home;

import kr.sofaware.slas.entity.Member;
import kr.sofaware.slas.repository.MemberRepository;
import kr.sofaware.slas.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final MemberService memberService;

    @GetMapping("/")
    public String home(Model model, Principal principal) {

        if(principal == null)
            return "redirect:/login";

        Member member = memberService.loadUserByUsername(principal.getName());

        model.addAttribute("member", member);
        return "home/home";
    }
}
