package kr.sofaware.slas.board.service;

import kr.sofaware.slas.board.entity.Board;
import kr.sofaware.slas.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoticeService implements BoardService {

    private final BoardRepository boardRepository;

    @Override
    public void create(Board board) {
        boardRepository.save(board);
    }

    @Override
    public Optional<Board> read(int boardId) {
        return boardRepository.findById(boardId);
    }

    @Override
    public void update(Board board) {

    }

    @Override
    public void delete(int boardId) {

    }

    @Override
    public List<Board> listAll() {
        return boardRepository.findAll();
    }

    @Override
    public void increaseViewCount(int boardId, HttpSession session) {

    }
}