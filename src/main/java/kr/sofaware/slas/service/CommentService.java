package kr.sofaware.slas.service;

import kr.sofaware.slas.entity.Comment;
import kr.sofaware.slas.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    public Optional<Comment> findById(int id) {
        return commentRepository.findById(id);
    }

    public void deleteById(int id) {
        commentRepository.deleteById(id);
    }
}
