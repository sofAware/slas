package kr.sofaware.slas.board;

import kr.sofaware.slas.service.BoardService;
import kr.sofaware.slas.entity.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/p")
@RequiredArgsConstructor
public class NoticeProfessorController {
    private final BoardService noticeService;

    @GetMapping("notice")
    public String readList(Model model,
                           @RequestParam("year-semester") Optional<String> yearSemester,
                           @RequestParam("syllabus") Optional<String> syllabusId) {

        // 사용자의 수강 학기와 과목들 조회

        // 게시판 목록 긁어오기
        List<Board> boards = noticeService.listAll();
        model.addAttribute("boards", boards);

        return "notice/pNotice";
    }
}
