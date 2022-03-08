package kr.sofaware.slas.repository;

import kr.sofaware.slas.entity.Lecture;
import kr.sofaware.slas.entity.LecturePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.thymeleaf.standard.expression.GreaterOrEqualToExpression;

import java.util.List;
import java.util.Optional;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, LecturePK> {
    List<Lecture> findAllByStudent_Id(@Param(value = "memberId") String id);
    List<Lecture> findAllBySyllabus_Id(String syllabusId);

    boolean existsBySyllabus_IdAndStudent_Id(String studentId, String syllabusId);

    Optional<Lecture> findFirstByStudent_IdOrderBySyllabusDesc(String studentId);
    List<Lecture> findAllBySyllabus_Id(String syllabusId);

    Optional<Lecture> findByStudent_IdAndSyllabus_Id(String studentId, String syllabusId);
    Lecture deleteByStudent_Id(String studentId);
}
