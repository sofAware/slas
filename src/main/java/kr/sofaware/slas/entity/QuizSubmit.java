package kr.sofaware.slas.entity;

import lombok.Getter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
public class QuizSubmit implements Serializable {
    @Id
    @ManyToOne
    @JoinColumns ({
        @JoinColumn(name = "syllabus_id"),
        @JoinColumn(name = "quiz_id"),
        @JoinColumn(name = "question_num")
    })
    private Quiz quiz;          // 퀴즈

    @Id
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;    // 학생

    private String answer;      // 제출한 답안
    private boolean isCorrect;  // 정답 유무

    @Override
    public int hashCode() {
        return Objects.hash(quiz, student);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || Hibernate.getClass(this) != Hibernate.getClass(obj))
            return false;

        QuizSubmit o = (QuizSubmit) obj;
        return Objects.equals(quiz, o.getQuiz()) &&
                Objects.equals(student, o.getStudent());
    }
}
