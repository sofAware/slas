package kr.sofaware.slas.repository;

import kr.sofaware.slas.entity.Board;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
class BoardRepositoryTest {

    @Autowired private BoardRepository boardRepository;

    @Test
    public void findAllByMember_Id() {
        List<Board> allByMember_id = boardRepository.findAllByMember_Id("123");

        allByMember_id.forEach(board -> System.out.println("board = " + board));
    }
}