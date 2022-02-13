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
    String name;                        // 과제 이름
    Date submitEnd;                    // 과제 제출 날짜

    @Builder
    public AssignmentDto(String name, Date submitEnd){
       this.name=name;
       this.submitEnd=submitEnd;
    }

    public AssignmentDto(Assignment entity){         // board entity 로부터 noticeDto 를 생성
        this.name=entity.getName();
        this.submitEnd=entity.getSubmitEnd();
    }
}
