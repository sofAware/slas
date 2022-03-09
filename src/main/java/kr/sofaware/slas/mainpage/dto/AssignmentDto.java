package kr.sofaware.slas.mainpage.dto;

import kr.sofaware.slas.entity.Assignment;
import kr.sofaware.slas.entity.Board;
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
    Date submitEnd;                     // 과제 제출 날짜

    @Builder
    public AssignmentDto(int id, String name, Date submitEnd){
        this.id=id;
        this.name=name;
        this.submitEnd=submitEnd;
    }

    public AssignmentDto(Assignment entity){         // assignment entity 로부터 assignmentDto 를 생성
        this.id=entity.getId();
        this.name=entity.getName();
        this.submitEnd=entity.getSubmitEnd();
    }
}
