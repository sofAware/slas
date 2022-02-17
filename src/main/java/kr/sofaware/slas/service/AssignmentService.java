package kr.sofaware.slas.service;

import kr.sofaware.slas.entity.Assignment;
import kr.sofaware.slas.mainpage.dto.AssignmentDto;
import kr.sofaware.slas.repository.AssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AssignmentService {
    private final AssignmentRepository assignmentRepository;

    /**
     * assignment 테이블에서 syllabus_id == :syllabus_id 이고 submitEnd >= :currentDate 인 레코드들을 찾아 제출 마감기한 순으로 오름차순 정렬 => (과제 제출 여부와는 상관없이) 아직 제출기한이 남은 과제들을 제출기한 빠른 순으로 정렬해 가져옴
     * @author 정지민
     * @param (syllabus_id, 현재 날짜)
     * @return 메인페이지 구성 시 사용하는 AssignmentDto 들의 리스트로 반환
     */
    public List<AssignmentDto> findBySyllabus_IdSubmitEndAfterOrderBySubmitEndAsc(String syllabus_id, Date currentDate){
        List<AssignmentDto> assignmentDtoList=new ArrayList<>();

        List<Assignment> assignmentList=assignmentRepository.findBySyllabus_IdSubmitEndAfterOrderBySubmitEndAsc(syllabus_id,currentDate);

        assignmentList.forEach(assignment -> assignmentDtoList.add(new AssignmentDto(assignment)));

        return assignmentDtoList;
    }
}
