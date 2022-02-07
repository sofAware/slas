package kr.sofaware.slas.board.controller;

import kr.sofaware.slas.auth.service.MemberService;
import kr.sofaware.slas.board.dto.BoardDTO;
import kr.sofaware.slas.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class NoticeController {

    private final MemberService memberService;
    private final BoardService noticeService;

    @GetMapping("notice")
    public String readList(Model model) {

        // Java side auth checking
//        boolean isProfessor = authentication.getAuthorities().contains("PROFESSOR");
//        if (isProfessor) {
//            Professor p = professorService.loadUserByUsername(authentication.getName());
//            model.addAttribute("name", p.getName());
//            model.addAttribute("role", "교수");
//        } else {
//            Student s = studentService.loadUserByUsername(authentication.getName());
//            model.addAttribute("name", s.getName());
//            model.addAttribute("role", "학생");
//        }

        // 게시판 목록 긁어오기
        BoardDTO[] boardDTOS = noticeService.listAll().stream().map(BoardDTO::from).toArray(BoardDTO[]::new);
        model.addAttribute("list", boardDTOS);

        return "notice/notice";
    }
}
