package kr.sofaware.slas.service;

import kr.sofaware.slas.entity.Board;
import kr.sofaware.slas.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LectureFileService implements BoardService {

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
        boardRepository.deleteById(boardId);
    }

    @Override
    public List<Board> listAll(String SyllabusId) {
        return boardRepository.findAllByCategoryAndSyllabus_Id(Board.CATEGORY_LECTURE_FILE, SyllabusId);
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
