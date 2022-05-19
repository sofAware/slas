//package kr.sofaware.slas.repository;
//
//import kr.sofaware.slas.entity.LectureVideo;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class LectureVideoRepositoryTest {
//
//    @Autowired private LectureVideoRepository lectureVideoRepository;
//
//    @Test
//    void findAllBySyllabus_Id() {
//        List<LectureVideo> lvs = lectureVideoRepository.findAllBySyllabus_Id("21-2-0101-3-0002-01");
//        System.out.println("lvs = " + lvs);
//    }
//}