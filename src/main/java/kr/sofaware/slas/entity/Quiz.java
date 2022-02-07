package kr.sofaware.slas.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@EqualsAndHashCode
@IdClass(QuizPK.class)
@Entity
@Getter
public class Quiz {//implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "syllabus_id")
    private Syllabus syllabus;          // 강의

    @Id private String id;              // 퀴즈 아이디 (<주차>-<회차>, 1-1, 1-2, ...)
    @Id private int questionNum;        // 문제 번호

    private String question;            // 문제
    private int category;               // 문제 유형 (1: 객관식, 2: 단답식, 3: 주관식)
    private String correctAnswer;       // 정답

    private String attachmentName;      // 첨부 파일 이름
    private String attachmentPath;      // 첨부 파일 경로

//    @Override
//    public int hashCode() {
//        return Objects.hash(syllabus, id, questionNum);
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) return true;
//        if (obj == null || Hibernate.getClass(this) != Hibernate.getClass(obj))
//            return false;
//
//        Quiz o = (Quiz) obj;
//        return Objects.equals(syllabus, o.getSyllabus()) &&
//                Objects.equals(id, o.getId()) &&
//                Objects.equals(questionNum, o.getQuestionNum());
//    }
}

class QuizPK implements Serializable{
    private Syllabus syllabus;          // 강의
    private String id;              // 퀴즈 아이디 (<주차>-<회차>, 1-1, 1-2, ...)
    private int questionNum;        // 문제 번호

}
