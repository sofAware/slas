package kr.sofaware.slas.service;

import kr.sofaware.slas.entity.Board;
import kr.sofaware.slas.entity.Comment;
import kr.sofaware.slas.repository.BoardRepository;
import kr.sofaware.slas.repository.CommentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QnaServiceTest {

    @Autowired private BoardRepository boardRepository;
    @Autowired private CommentRepository commentRepository;

    @Test
    void delete() {
        Optional<Board> oBoard = boardRepository.findById(54);
        Board board = oBoard.get();

        Optional<Comment> oComment = commentRepository.findById(10);
        Comment comment = oComment.get();

        System.out.println("before removing board.getComments() = " + board.getComments());

        board.deleteCommentById(comment.getId());

        System.out.println("after removing board.getComments() = " + board.getComments());

        boardRepository.save(board);
        commentRepository.deleteById(comment.getId());
    }
}