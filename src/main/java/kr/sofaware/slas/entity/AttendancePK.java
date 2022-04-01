package kr.sofaware.slas.entity;

import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
public class AttendancePK implements Serializable {
    private String student;        // 학생
    private String syllabus;      // 강의
}
