package kr.sofaware.slas.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class LectureVideoPK implements Serializable {
    private String syllabus;    // 강의
    private String id;          // 강의영상 아이디 (<주차>-<회차>, 1-1, 1-2, ...)
}
