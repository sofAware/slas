package kr.sofaware.slas.service;

import kr.sofaware.slas.entity.Board;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Service
public interface BoardService {
    void create(Board board);                                   // 작성
    Optional<Board> read(int boardId);                          // 상세 보기
    void update(Board board);                                   // 수정
    void delete(int boardId);                                   // 삭재
    List<Board> listAll();                                      // 전체 목록
    List<Board> listAll(String SyllabusId);                     // 학정번호로 조회 (카고리별로 각 서비스를 만들어서 Board Entity의 상수를 사용해서 Repository 조회)
    void increaseViewCount(int boardId, HttpSession session);   // 조회
}
