package kr.sofaware.slas.repository;

import kr.sofaware.slas.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {
    List<Assignment> findAllBySyllabus_Id(String syllabusId);

    @Query("SELECT a FROM Assignment a WHERE a.syllabus.id = :syllabus_id AND a.submitEnd >= :currentDate ORDER BY a.submitEnd ASC")        //지민
    List<Assignment> findBySyllabus_IdSubmitEndAfterOrderBySubmitEndAsc(String syllabus_id, Date currentDate);

    List<Assignment> findBySyllabus_IdOrderBySubmitEndAsc(String syllabus_id);          //지민

    List<Assignment> findBySyllabus_Id(String syllabus_id);

    int countBySyllabus_Id(String syllabus_id);
}

