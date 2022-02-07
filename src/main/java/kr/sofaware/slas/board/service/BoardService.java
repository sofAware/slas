package kr.sofaware.slas.board.service;

import kr.sofaware.slas.board.entity.Board;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Service
public interface BoardService {
    void create(Board board);   // 작성
    Optional<Board> read(int boardId);    // 상세 보기
    void update(Board board);   // 수정
    void delete(int boardId);   // 석재
    List<Board> listAll();      // 전체 목록
    void increaseViewCount(int boardId, HttpSession session);   // 조회
}
