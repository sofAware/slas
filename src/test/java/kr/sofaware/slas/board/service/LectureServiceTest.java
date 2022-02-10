package kr.sofaware.slas.board.service;

import kr.sofaware.slas.entity.Syllabus;
import kr.sofaware.slas.service.LectureService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest
class LectureServiceTest {
    @Autowired
    LectureService lectureService;

    @Test
    void mapAllByStudentId() {
        Map<String, List<Syllabus>> map = lectureService.mapAllByStudentId("2019010101");
        System.out.println("map = " + map);
    }
}