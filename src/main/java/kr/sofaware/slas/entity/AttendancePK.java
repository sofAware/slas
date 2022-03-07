package kr.sofaware.slas.entity;

import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
public class AttendancePK implements Serializable {
    private Member student;        // 학생
    private Syllabus syllabus;      // 강의
}
