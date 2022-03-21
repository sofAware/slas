package kr.sofaware.slas.repository;

import kr.sofaware.slas.entity.LectureVideo;
import kr.sofaware.slas.entity.LectureVideoPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LectureVideoRepository extends JpaRepository<LectureVideo, LectureVideoPK> {
    Optional<LectureVideo> findBySyllabus_IdAndId(String syllabusId, int id);
    List<LectureVideo> findAllBySyllabus_Id(String syllabusId);
}
