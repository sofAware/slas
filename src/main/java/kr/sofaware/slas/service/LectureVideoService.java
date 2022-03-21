package kr.sofaware.slas.service;

import kr.sofaware.slas.entity.LectureVideo;
import kr.sofaware.slas.repository.LectureVideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
