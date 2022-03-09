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

    @Test
    void existsByIdAndProfessor_Id() {
        boolean b = syllabusRepository.existsByIdAndProfessor_Id("21-1-0201-1-0001-02", "20102");
        System.out.println("b = " + b);
    }

}