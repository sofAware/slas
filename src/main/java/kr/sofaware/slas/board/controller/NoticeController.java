package kr.sofaware.slas.board.controller;

import kr.sofaware.slas.board.dto.BoardDTO;
import kr.sofaware.slas.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class NoticeController {
    private final BoardService noticeService;

    @GetMapping("notice")
    public String readList(Model model) {

        // 게시판 목록 긁어오기
        BoardDTO[] boardDTOS = noticeService.listAll().stream().map(BoardDTO::from).toArray(BoardDTO[]::new);
        model.addAttribute("list", boardDTOS);

        return "notice/notice";
    }
}
