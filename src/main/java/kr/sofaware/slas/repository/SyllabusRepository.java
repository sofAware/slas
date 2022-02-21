package kr.sofaware.slas.repository;

import kr.sofaware.slas.entity.Syllabus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SyllabusRepository extends JpaRepository<Syllabus, String> {
    List<Syllabus> findAllByProfessor_Id(String professorId);
    boolean existsByIdAndProfessor_Id(String id, String professorId);
    Optional<Syllabus> findFirstByProfessor_IdOrderByIdDesc(String professorId);
}
