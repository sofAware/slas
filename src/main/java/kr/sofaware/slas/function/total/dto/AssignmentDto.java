package kr.sofaware.slas.function.total.dto;

import kr.sofaware.slas.entity.Assignment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class AssignmentDto {
    int id;                             // 과제 id
    String name;                        // 과제 이름

    Date submitStart;                   // 과제 제출 시작
    Date submitEnd;                     // 과제 제출 날짜

    Boolean submitTrue=false;                 // 제출 상태

    @Builder
    public AssignmentDto(int id, String name, Date submitStart, Date submitEnd, Boolean submitTrue){
        this.id=id;
        this.name=name;
        this.submitStart=submitStart;
        this.submitEnd=submitEnd;
        this.submitTrue=submitTrue;
    }

    public AssignmentDto(Assignment entity){         // assignment entity 로부터 assignmentDto 를 생성
        this.id=entity.getId();
        this.name=entity.getName();
        this.submitStart=entity.getSubmitStart();
        this.submitEnd=entity.getSubmitEnd();
    }
}
