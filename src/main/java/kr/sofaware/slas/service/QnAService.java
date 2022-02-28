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
public class QnAService implements BoardService{
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
    public List<Board> listAll(String SyllabusId) {
        return boardRepository.findAllByCategoryAndSyllabus_Id(Board.CATEGORY_QNA, SyllabusId);
    }

    @Override
    public void increaseViewCount(int boardId, HttpSession session) {

    }

    public List<Board> getQnAListOrderByDateDesc(String syllabusId){
        return boardRepository.findByCategoryAndSyllabus_IdOrderByDateDesc(Board.CATEGORY_QNA,syllabusId);
    }
}
