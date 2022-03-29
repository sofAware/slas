package kr.sofaware.slas.service;

import kr.sofaware.slas.entity.LectureVideo;
import kr.sofaware.slas.repository.LectureVideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LectureVideoService {

    private final LectureVideoRepository lectureVideoRepository;

    public Optional<LectureVideo> get(String syllabusId, int id) {
        return lectureVideoRepository.findBySyllabus_IdAndId(syllabusId, id);
    }

    public List<LectureVideo> listAll(String syllabusId) {
        return lectureVideoRepository.findAllBySyllabus_Id(syllabusId);
    }

    public void save(LectureVideo lectureVideo) {
        lectureVideoRepository.save(lectureVideo);
    }

    public void delete(LectureVideo entity) {
        lectureVideoRepository.delete(entity);
    }

    /**
     * lecture_video 테이블에서 syllabus_id == :syllabus_id 이고 attendanceEnd >= :currentDate 인 레코드들을 찾아 수강 마감기한 순으로 오름차순 정렬 => (강의 수강 여부와는 상관없이) 아직 기한이 남은 강의들을 마감기한 빠른 순으로 정렬해 가져옴
     * @author 정지민
     * @param (syllabus_id, 현재 날짜)
     * @return LectureVideo 들의 리스트로 반환
     */
    public List<LectureVideo> findBySyllabus_IdSubmitEndAfterOrderBySubmitEndAsc(String syllabus_id, Date currentDate){
        List<LectureVideo> lectureVideoList=lectureVideoRepository.findBySyllabus_IdSubmitEndAfterOrderBySubmitEndAsc(syllabus_id,currentDate);

        return lectureVideoList;
    }
}
