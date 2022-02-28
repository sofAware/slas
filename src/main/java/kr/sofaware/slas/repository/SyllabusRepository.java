package kr.sofaware.slas.repository;

import kr.sofaware.slas.entity.Syllabus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SyllabusRepository extends JpaRepository<Syllabus, String> {
    List<Syllabus> findAllByProfessor_Id(String id);
    List<Syllabus> findAllByNameOrProfessor_Name(String syllabusName, String ProfessorName);
}
