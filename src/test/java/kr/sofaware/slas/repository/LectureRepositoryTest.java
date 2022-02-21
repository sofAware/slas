package kr.sofaware.slas.repository;

import kr.sofaware.slas.entity.Lecture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class LectureRepositoryTest {

    @Autowired
    private LectureRepository lectureRepository;

    @Test
    void findAllByStudent_Id() {
        List<Lecture> allByStudent_id = lectureRepository.findAllByStudent_Id("2019010101");
        System.out.println("allByStudent_id = " + allByStudent_id);
        allByStudent_id.forEach(lecture -> System.out.println("lecture : " + lecture));
    }

    @Test
    void existsByStudent_IdAndSyllabus_Id() {
        boolean b = lectureRepository.existsByStudent_IdAndSyllabus_Id("2019010101", "21-1-0201-1-0001-02");
        System.out.println("b = " + b);
    }
}