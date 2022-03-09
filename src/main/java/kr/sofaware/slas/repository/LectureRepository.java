package kr.sofaware.slas.repository;

import kr.sofaware.slas.entity.Lecture;
import kr.sofaware.slas.entity.LecturePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import org.thymeleaf.standard.expression.GreaterOrEqualToExpression;

import java.util.Optional;
import java.util.List;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, LecturePK> {
    List<Lecture> findByStudent_Id(String id);      //student_id 로 lecture 불러오기

    @Query("SELECT l FROM Lecture l WHERE l.student.id = :id AND l.syllabus.id LIKE :yearSemester%")
    List<Lecture> findByIdAndYearSemester(String id, String yearSemester);              //학생이 해당 년도-학기 (ex. "21-2") 에 들은 수강목록을 반환    //지민 -> 근데 이거 사용안할듯

    List<Lecture> findAllByStudent_Id(@Param(value = "memberId") String id);
    List<Lecture> findAllBySyllabus_Id(String syllabusId);

    boolean existsBySyllabus_IdAndStudent_Id(String studentId, String syllabusId);

    Optional<Lecture> findFirstByStudent_IdOrderBySyllabusDesc(String studentId);

    Optional<Lecture> findByStudent_IdAndSyllabus_Id(String studentId, String syllabusId);
    Lecture deleteByStudent_Id(String studentId);
}
