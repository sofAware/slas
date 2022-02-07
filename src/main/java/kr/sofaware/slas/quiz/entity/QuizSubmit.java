package kr.sofaware.slas.quiz.entity;

import kr.sofaware.slas.auth.entity.Member;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@IdClass(QuizSubmitPK.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class QuizSubmit {
    @Id
    @ManyToOne
    @JoinColumns ({
        @JoinColumn(name = "syllabus_id", referencedColumnName = "syllabus_id"),
        @JoinColumn(name = "quiz_id", referencedColumnName = "id"),
        @JoinColumn(name = "question_num", referencedColumnName = "question_num")
    })
    private Quiz quiz;          // 퀴즈

    @Id
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Member student;    // 학생

    private String answer;      // 제출한 답안
    private boolean isCorrect;  // 정답 유무
}

@EqualsAndHashCode
class QuizSubmitPK implements Serializable {
    private Quiz quiz;          // 퀴즈
    private Member student;
}
