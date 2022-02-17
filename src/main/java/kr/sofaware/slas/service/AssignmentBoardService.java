package kr.sofaware.slas.service;


import kr.sofaware.slas.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AssignmentBoardService {                       // board 게시판의 글들 중 "과제 제출 글" 관련 서비스들을 어따 둘까 하다가 일단 여기에 넣음.. 클래스 이름 맘에 안들어..ㅜ
    private final BoardRepository boardRepository;

    /**
     * board 테이블에서 과제 id = :assignment_id, 작성자 id = :member_id 인 레코드가 존재하는지 확인 => 해당 과제에 대해 학생이 제출했는지 여부 확인
     * @author 정지민
     * @param (assignment_id, 학생 학번)
     * @return 존재 -> true, 없으면 -> false
     */
    public Boolean existsByAssignment_IdAndMember_Id(int assignment_id, String member_id){
        return boardRepository.existsByAssignment_IdAndMember_Id(assignment_id,member_id);
    }
}
