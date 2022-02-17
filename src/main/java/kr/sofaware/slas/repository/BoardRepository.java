package kr.sofaware.slas.repository;

import kr.sofaware.slas.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {
    List<Board> findAllByMember_Id(String memberId);

    List<Board> findAllByCategoryAndSyllabus_Id(int category, String syllabusId);

    //List<Board> findAllByMember_Id(@Param(value = "memberId") String memberId);

    List<Board> findFirst3ByCategoryAndSyllabus_IdOrderByDateDesc(int category, String syllabus_id);

    Boolean existsByAssignment_IdAndMember_Id(int assignment_id, String member_id);
}
