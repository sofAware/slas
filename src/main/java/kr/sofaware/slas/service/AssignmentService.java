package kr.sofaware.slas.service;

import kr.sofaware.slas.entity.Assignment;
import kr.sofaware.slas.repository.AssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;

    public Assignment save(Assignment assignment) {
        return assignmentRepository.save(assignment);
    }

    public Optional<Assignment> read(int assignmentId) {
        return assignmentRepository.findById(assignmentId);
    }

    public void delete(int assignmentId) {
        assignmentRepository.deleteById(assignmentId);
    }

    public List<Assignment> listAll(String SyllabusId) {
        return assignmentRepository.findAllBySyllabus_Id(SyllabusId);
    }
}
