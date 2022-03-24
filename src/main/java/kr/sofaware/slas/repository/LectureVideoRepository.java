package kr.sofaware.slas.repository;

import kr.sofaware.slas.entity.LectureVideo;
import kr.sofaware.slas.entity.LectureVideoPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface LectureVideoRepository extends JpaRepository<LectureVideo, LectureVideoPK> {
    Optional<LectureVideo> findBySyllabus_IdAndId(String syllabusId, String id);
    List<LectureVideo> findAllBySyllabus_Id(String syllabusId);

    @Query("SELECT lv FROM LectureVideo lv WHERE lv.syllabus.id = :syllabus_id AND lv.attendanceEnd >= :currentDate ORDER BY lv.attendanceEnd ASC")        //지민
    List<LectureVideo> findBySyllabus_IdSubmitEndAfterOrderBySubmitEndAsc(String syllabus_id, Date currentDate);
}
