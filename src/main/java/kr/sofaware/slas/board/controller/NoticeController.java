package kr.sofaware.slas.board.controller;

import kr.sofaware.slas.board.service.BoardService;
import kr.sofaware.slas.entity.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class NoticeController {
    private final BoardService noticeService;

    @GetMapping("notice")
    public String readList(Model model) {

        // 게시판 목록 긁어오기
        List<Board> boards = noticeService.listAll();
        model.addAttribute("list", boards);

        return "notice/notice";
    }
}
