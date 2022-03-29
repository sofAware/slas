package kr.sofaware.slas.repository;

import kr.sofaware.slas.entity.Quiz;
import kr.sofaware.slas.entity.QuizPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, QuizPK> {
    List<Quiz> findAllBySyllabus_Id(String syllabusId);
    List<Quiz> findBySyllabus_IdOrderByQuestionNumAsc(String id);
    List<Quiz> findBySyllabus_IdAndId(String syllabus_id, String id);


    List<Quiz> findAllBySyllabus_IdAndId(String syllabus_id, String id);
}
