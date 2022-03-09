package kr.sofaware.slas.service;

import kr.sofaware.slas.entity.Assignment;
import kr.sofaware.slas.repository.AssignmentRepository;
import kr.sofaware.slas.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final BoardRepository boardRepository;

    /**
     * @param assignment
     * @author 양경호
     * @return
     */
    public Assignment save(Assignment assignment) {
        return assignmentRepository.save(assignment);
    }

    /**
     * @author 양경호
     */
    public Optional<Assignment> read(int assignmentId) {
        return assignmentRepository.findById(assignmentId);
    }

    /**
     * @author 양경호
     */
    public void delete(int assignmentId) {
        assignmentRepository.deleteById(assignmentId);
    }

    /**
     * @author 양경호
     */
    public List<Assignment> listAll(String SyllabusId) {
        return assignmentRepository.findAllBySyllabus_Id(SyllabusId);
    }

    /**
     * assignment 테이블에서 syllabus_id == :syllabus_id 이고 submitEnd >= :currentDate 인 레코드들을 찾아 제출 마감기한 순으로 오름차순 정렬 => (과제 제출 여부와는 상관없이) 아직 제출기한이 남은 과제들을 제출기한 빠른 순으로 정렬해 가져옴
     * @author 정지민
     * @param (syllabus_id, 현재 날짜)
     * @return Assignment 들의 리스트로 반환
     */
    public List<Assignment> findBySyllabus_IdSubmitEndAfterOrderBySubmitEndAsc(String syllabus_id, Date currentDate){
        List<Assignment> assignmentList=assignmentRepository.findBySyllabus_IdSubmitEndAfterOrderBySubmitEndAsc(syllabus_id,currentDate);

        return assignmentList;
    }

    /**
     * assignment 테이블에서 syllabus_id == :syllabus_id 인 레코드들을 찾아 제출 마감기한 순으로 오름차순 정렬 => (과제 제출 여부와는 상관없이) 해당 과목에 대한 과제들을 제출기한 빠른 순으로 정렬해 가져옴
     * @author 정지민
     * @param syllabus_id
     * @return Assignment 들의 리스트로 반환
     */
    public List<Assignment> findBySyllabus_IdOrderBySubmitEndAsc(String syllabus_id){
        List<Assignment> assignmentList=assignmentRepository.findBySyllabus_IdOrderBySubmitEndAsc(syllabus_id);

        return assignmentList;
    }

    /**
     * board 테이블에서 과제 id = :assignment_id, 작성자 id = :member_id 인 레코드가 존재하는지 확인 => 해당 과제에 대해 학생이 제출했는지 여부 확인
     * @author 정지민
     * @param (assignment_id, 학생 학번)
     * @return 존재 -> true, 없으면 -> false
     */
    public Boolean existsByAssignment_IdAndMember_Id(int assignment_id, String member_id){
        return boardRepository.existsByAssignment_IdAndMember_Id(assignment_id,member_id);
    }

    /**
     * assignment 테이블에서 해당 과목의 과제들이 몇개인지 count
     * @author 정지민
     * @param syllabus_id
     * @return 해당 과목 과제들의 개수
     */
    public int countBySyllabus_Id(String syllabus_id){
        return assignmentRepository.countBySyllabus_Id(syllabus_id);
    }

    /**
     * assignment 테이블에서 해당 과목의 과제들 중 이 학생이 제출완료한 과제가 몇개인지 count
     * @author 정지민
     * @param syllabus_id, student_id
     * @return 학생이 해당 과목에 대해 제출한 과제들의 개수
     */
    public int countSubmittedAssignment(String syllabus_id, String student_id){
        List<Assignment> assignmentList=assignmentRepository.findBySyllabus_Id(syllabus_id);
        int submittedCount=0;

        for(Assignment a : assignmentList)
            if(existsByAssignment_IdAndMember_Id(a.getId(),student_id))
                submittedCount++;

        return submittedCount;
    }
}
