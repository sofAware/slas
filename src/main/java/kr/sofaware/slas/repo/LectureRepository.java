package kr.sofaware.slas.repo;

import kr.sofaware.slas.entity.Lecture;
import kr.sofaware.slas.entity.LecturePK;
import kr.sofaware.slas.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, LecturePK> {
    List<Lecture> findByStudent_Id(String id);      //student_id 로 lecture 불러오기

    @Query("SELECT l FROM Lecture l WHERE l.student.id = :id AND l.syllabus.id LIKE :yearSemester%")
    List<Lecture> findByIdAndYearSemester(String id, String yearSemester);

}
