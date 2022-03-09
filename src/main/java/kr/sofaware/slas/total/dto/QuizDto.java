package kr.sofaware.slas.total.dto;

import kr.sofaware.slas.entity.Assignment;
import kr.sofaware.slas.entity.Quiz;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class QuizDto {
    private String id;                                  // 퀴즈 id
    private String name;                                // 퀴즈명

    private Date submitStart;                           // 응시 시작일
    private Date submitEnd;                             // 응시 마감일

    private Boolean submitted=false;                     // 응시 완료 여부
    private int totalScore=0;                            // 해당 퀴즈의 총점
    private int acquiredScore=0;                         // 취득 점수

    @Builder
    public QuizDto(String id, String name, Date submitStart, Date submitEnd, Boolean submitted, int totalScore, int acquiredScore){
        this.id=id;
        this.name=name;
        this.submitStart=submitStart;
        this.submitEnd=submitEnd;
        this.submitted=submitted;
        this.totalScore=totalScore;
        this.acquiredScore=acquiredScore;
    }

    public QuizDto(Quiz entity){                           // entity 로부터 dto 생성
        this.id=entity.getId();
        this.name=entity.getName();
        this.submitStart=entity.getSubmitStart();
        this.submitEnd=entity.getSubmitEnd();
    }
}
