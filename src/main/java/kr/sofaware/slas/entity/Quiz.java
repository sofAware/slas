package kr.sofaware.slas.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@ToString
@IdClass(QuizPK.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Quiz {
    @Id
    @ManyToOne
    @JoinColumn(name = "syllabus_id")
    private Syllabus syllabus;          // 강의

    @Id
    @Column(name = "id")
    private String id;              // 퀴즈 아이디 (<주차>-<회차>, 1-1, 1-2, ...)

    @Id
    @Column(name = "question_num")
    private int questionNum;        // 문제 번호

    private String question;            // 문제
    private int category;               // 문제 유형 (1: 객관식, 2: 단답식, 3: 주관식)
    private String correctAnswer;       // 정답

    private String attachmentName;      // 첨부 파일 이름
    private String attachmentPath;      // 첨부 파일 경로
}

@EqualsAndHashCode
class QuizPK implements Serializable{
    private Syllabus syllabus;          // 강의
    private String id;              // 퀴즈 아이디 (<주차>-<회차>, 1-1, 1-2, ...)
    private int questionNum;        // 문제 번호

}
