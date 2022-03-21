package kr.sofaware.slas.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class LectureVideoPK implements Serializable {
    private String syllabus;    // 강의
    private int id;             // 강의영상 주차
}
