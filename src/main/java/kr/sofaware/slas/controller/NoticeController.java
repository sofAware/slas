package kr.sofaware.slas.controller;

import kr.sofaware.slas.dto.BoardDTO;
import kr.sofaware.slas.entity.Professor;
import kr.sofaware.slas.entity.Student;
import kr.sofaware.slas.service.BoardService;
import kr.sofaware.slas.service.ProfessorService;
import kr.sofaware.slas.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class NoticeController {
    private final ProfessorService professorService;
    private final StudentService studentService;

    private final BoardService noticeService;

    @GetMapping("notice")
    public String readList(Authentication authentication, Model model) {

        // 교수, 학생 분류
        boolean isProfessor = authentication.getAuthorities().contains("PROFESSOR");
        if (isProfessor) {
            Professor p = professorService.loadUserByUsername(authentication.getName());
            model.addAttribute("name", p.getName());
            model.addAttribute("role", "교수");
        } else {
            Student s = studentService.loadUserByUsername(authentication.getName());
            model.addAttribute("name", s.getName());
            model.addAttribute("role", "학생");
        }

        // 게시판 목록 긁어오기
        BoardDTO[] boardDTOS = noticeService.listAll().stream().map(BoardDTO::from).toArray(BoardDTO[]::new);
        model.addAttribute("list", boardDTOS);

        return "notice/notice";
    }
}
