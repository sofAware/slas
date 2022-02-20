package kr.sofaware.slas.repository;

import kr.sofaware.slas.entity.Syllabus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SyllabusRepository extends JpaRepository<Syllabus, String> {
    List<Syllabus> findAllByProfessor_Id(String id);

}
