package kr.sofaware.slas.repository;

import kr.sofaware.slas.entity.QuizSubmit;
import kr.sofaware.slas.entity.QuizSubmitPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface QuizSubmitRepository extends JpaRepository<QuizSubmit, QuizSubmitPK> {
    int countByStudent_IdAndQuiz_Syllabus_IdAndQuiz_Id(String studentId, String syllabusId, String quizId);

    List<QuizSubmit> findAllByQuiz_Syllabus_Id(String syllabusId);

    List<QuizSubmit> findByQuiz_Syllabus_IdAndQuiz_Id(String syllabus_id, String id);

    Optional<QuizSubmit> findByQuiz_SyllabusId(String syllabusId);
}

