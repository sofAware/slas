package kr.sofaware.slas.entity;

import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
public class QuizPK implements Serializable {
    private Syllabus syllabus;          // 강의
    private String id;              // 퀴즈 아이디 (<주차>-<회차>, 1-1, 1-2, ...)
    private int questionNum;        // 문제 번호

}
