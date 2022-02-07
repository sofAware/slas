package kr.sofaware.slas.repository;

import kr.sofaware.slas.entity.Board;
import kr.sofaware.slas.entity.Professor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@SpringBootTest
@Transactional
class BoardRepositoryTest {
    @Autowired private ProfessorRepository professorRepository;
    @Autowired private BoardRepository boardRepository;

    Random rand = new Random();

    @Test
    void addBoardAndViewTest() {
        List<Professor> professors = professorRepository.findAll();
        Professor professor = professors.get(rand.nextInt(professors.size()));

        Board saveBoard = Board.builder()
                .category(1)
                .title("제목이에용")
                .content("내용입니당")
                .professor(professor)
                .date(new Date())
                .view(rand.nextInt(900) + 100)
                .build();

        System.out.println("boardId before save: " + saveBoard.getId());
        boardRepository.save(saveBoard);
        System.out.println("boardId after save: " + saveBoard.getId());
        System.out.println("saveBoard = " + saveBoard);
        // 저장 후 번호가 자동 갱신되는 것들 볼 수 있었음.
        // 트랜잭션 넣어뒀는데도 항상 증가되어 누적되고 있긴함.

        Board findBoard = boardRepository.findById(saveBoard.getId()).get();
        System.out.println("findBoard = " + findBoard);

        Assertions.assertThat(saveBoard).isEqualTo(findBoard);
    }
}