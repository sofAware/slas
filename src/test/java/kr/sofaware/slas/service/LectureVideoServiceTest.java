package kr.sofaware.slas.service;

import kr.sofaware.slas.entity.LectureVideo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LectureVideoServiceTest {

    @Autowired private LectureVideoService lectureVideoService;

    @Test
    void listAll() {
        List<LectureVideo> lvs = lectureVideoService.listAll("21-2-0101-3-0002-01");
        System.out.println("lvs = " + lvs);
    }
}