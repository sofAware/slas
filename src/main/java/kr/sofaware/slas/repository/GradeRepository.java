package kr.sofaware.slas.repository;

import kr.sofaware.slas.entity.Grade;
import kr.sofaware.slas.entity.GradePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade, GradePK> {
    List<Grade> findAllByStudent_Id(String studentId);
    Optional<Grade> findByStudent_IdAndYearAndSemester(String studentId, int year, int semester);
    List<Optional<Grade>> findAllByYearAndSemesterOrderByGradeAvgDesc(int year, int semester);
}
