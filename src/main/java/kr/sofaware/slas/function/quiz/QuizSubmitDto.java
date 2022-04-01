package kr.sofaware.slas.function.quiz;

import kr.sofaware.slas.entity.Member;
import kr.sofaware.slas.entity.Quiz;
import kr.sofaware.slas.entity.QuizPK;
import lombok.*;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
public class QuizSubmitDto {
    private QuizPK quiz;          // 퀴즈
    private String student;    // 학생
    private String answer;      // 제출한 답안
    //private boolean isCorrect;  // 정답 유무
    //private int score;          // 취득 점수
}
