package kr.sofaware.slas.repository;

import kr.sofaware.slas.entity.LectureVideo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LectureVideoRepository extends JpaRepository<LectureVideo, String> {
    Optional<LectureVideo> findBySyllabus_IdAndId(String syllabusId, String id);
    List<LectureVideo> findAllBySyllabus_Id(String syllabusId);
}
