package kr.sofaware.slas.repository;

import kr.sofaware.slas.entity.Syllabus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SyllabusRepositoryTest {

    @Autowired private SyllabusRepository syllabusRepository;

    @Test
    void findAllByProfessor_Id() {
        List<Syllabus> syllabusList = syllabusRepository.findAllByProfessor_Id("10101");
        System.out.println("syllabusList = " + syllabusList);
    }
}