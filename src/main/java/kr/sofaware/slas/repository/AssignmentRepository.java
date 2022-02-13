package kr.sofaware.slas.repository;

import kr.sofaware.slas.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {
    List<Assignment> findBySyllabus_IdSubmitEndAfterOrderBySubmitEndAsc(String syllabus_id, Date currentDate);
}
