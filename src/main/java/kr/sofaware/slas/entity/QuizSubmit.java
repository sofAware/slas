package kr.sofaware.slas.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@EqualsAndHashCode
@IdClass(QuizSubmitPK.class)
@Entity
@Getter
public class QuizSubmit implements Serializable {
    @Id
    @ManyToOne
    @JoinColumns ({
        @JoinColumn(name = "syllabus_id", referencedColumnName = "syllabus_id"),
        @JoinColumn(name = "quiz_id", referencedColumnName = "id"),
        @JoinColumn(name = "question_num", referencedColumnName = "questionNum")
    })
    private Quiz quiz;          // 퀴즈

    @Id
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;    // 학생

    private String answer;      // 제출한 답안
    private boolean isCorrect;  // 정답 유무

//    @Override
//    public int hashCode() {
//        return Objects.hash(quiz, student);
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) return true;
//        if (obj == null || Hibernate.getClass(this) != Hibernate.getClass(obj))
//            return false;
//
//        QuizSubmit o = (QuizSubmit) obj;
//        return Objects.equals(quiz, o.getQuiz()) &&
//                Objects.equals(student, o.getStudent());
//    }
}

class QuizSubmitPK implements Serializable{
    private Quiz quiz;          // 퀴즈
    private Student student;
}
