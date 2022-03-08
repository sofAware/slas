package kr.sofaware.slas.service;

import kr.sofaware.slas.entity.Board;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface BoardService {
    void save(Board board);                                     // 저장
    Optional<Board> read(int boardId);                          // 상세 보기
    void delete(int boardId);                                   // 삭제
    List<Board> listAll(String SyllabusId);                     // 학정번호로 조회 (카고리별로 각 서비스를 만들어서 Board Entity의 상수를 사용해서 Repository 조회)
    void increaseViewCount(int boardId);                        // 조회수 증가
}
