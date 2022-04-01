package kr.sofaware.slas.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class QuizPK implements Serializable {
    private String syllabus;        // 강의
    private String id;              // 퀴즈 아이디 (<주차>-<회차>, 1-1, 1-2, ...)
    private int questionNum;        // 문제 번호

}
