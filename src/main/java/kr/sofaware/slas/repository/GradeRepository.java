package kr.sofaware.slas.repository;

import kr.sofaware.slas.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grade, String> {
    List<Grade> findAllByStudent_Id(String studentId);
}
