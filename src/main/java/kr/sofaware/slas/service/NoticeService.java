package kr.sofaware.slas.service;

import kr.sofaware.slas.entity.Board;
import kr.sofaware.slas.mainpage.dto.NoticeDto;
import kr.sofaware.slas.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoticeService implements BoardService {

    private final BoardRepository boardRepository;

    @Override
    public void save(Board board) {
        board.setCategory(Board.CATEGORY_NOTICE);
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

    /**
     * board 테이블에서 ( category 는 1이고, 즉 공지사항이고 && syllabus_id 는 syllabusDtoList.get(i).id ) 인 것들을 찾아서 등록일 빠른 순으로(datetime 내림차순) 정렬 => 위에서부터 레코드 3개만 가져오기 => noticeDtoList 에 noticeDto 로 add
     * 요약하자면 해당 과목의 가장 최신 공지 3개를 가져와줌 !!
     * @author 정지민
     * @param (board 테이블의 카테고리, syllabus_id)
     * @return 메인 페이지에서 사용하는 NoticeDto 들의 List
     */
    public List<NoticeDto> findFirst3ByCategoryAndSyllabus_IdOrderByDateDesc(String syllabus_id){
        List<NoticeDto> noticeDtoList=new ArrayList<>();

        List<Board> boardList=boardRepository.findFirst3ByCategoryAndSyllabus_IdOrderByDateDesc(Board.CATEGORY_NOTICE,syllabus_id);

        boardList.forEach(board -> noticeDtoList.add(new NoticeDto(board)));

        return noticeDtoList;
    }
}
