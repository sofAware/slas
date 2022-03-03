package kr.sofaware.slas.service;

import kr.sofaware.slas.entity.LectureVideo;
import kr.sofaware.slas.entity.Syllabus;
import kr.sofaware.slas.repository.SyllabusRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LectureVideoServiceTest {

    @Autowired private LectureVideoService lectureVideoService;
    @Autowired private SyllabusRepository syllabusRepository;

    @Test
    void listAll() {
        List<LectureVideo> lvs = lectureVideoService.listAll("21-2-0101-3-0002-01");
        System.out.println("lvs = " + lvs);
    }

    @Test
    void save() {
        Syllabus s = syllabusRepository.getById("21-2-0101-3-0002-01");
        LectureVideo lectureVideo = new LectureVideo(s, "3-5", "타이틀틀틀", new Date(), new Date(), 13, "path");

        lectureVideoService.save(lectureVideo);
    }
}