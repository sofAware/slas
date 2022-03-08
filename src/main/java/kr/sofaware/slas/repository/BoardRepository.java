package kr.sofaware.slas.repository;

import kr.sofaware.slas.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {
    List<Board> findAllByMember_Id(String memberId);

    List<Board> findAllByCategoryAndSyllabus_Id(int category, String syllabusId);

    Optional<Board> findByAssignment_IdAndMember_Id(int assignmentId, String memberId);
}
