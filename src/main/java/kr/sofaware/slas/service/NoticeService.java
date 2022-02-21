package kr.sofaware.slas.service;

import kr.sofaware.slas.entity.Board;
import kr.sofaware.slas.repository.BoardRepository;
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
    public void save(Board board) {
        boardRepository.save(board);
    }

    @Override
    public Optional<Board> read(int boardId) {
        return boardRepository.findById(boardId);
    }

    @Override
    public void delete(int boardId) {

    }

    @Override
    public List<Board> listAll() {
        return boardRepository.findAll();
    }

    @Override
    public List<Board> listAll(String SyllabusId) {
        return boardRepository.findAllByCategoryAndSyllabus_Id(Board.CATEGORY_NOTICE, SyllabusId);
    }

    @Override
    public void increaseViewCount(int boardId) {
        Optional<Board> oBoard = boardRepository.findById(boardId);
        if (oBoard.isEmpty()) return;
        Board board = oBoard.get();

        board.increaseViewCount();
        boardRepository.save(board);
    }
}
