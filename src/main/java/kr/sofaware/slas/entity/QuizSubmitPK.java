package kr.sofaware.slas.entity;

import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
public class QuizSubmitPK implements Serializable {
    private Quiz quiz;          // 퀴즈
//    private String syllabus;
//    private String id;
//    private int questionNum;
    private String student;
}
