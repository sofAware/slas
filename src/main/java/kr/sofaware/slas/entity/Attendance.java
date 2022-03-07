package kr.sofaware.slas.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@ToString
@IdClass(AttendancePK.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class   Attendance {//implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Member student;        // 학생

    @Id
    @ManyToOne
    @JoinColumn(name = "syllabus_id")
    private Syllabus syllabus;      // 강의

    private int week1, week2, week3, week4, week5, week6, week7, week8, week9, week10;  // 출석 1~10주차 (1: 출석, 2: 지각, 3: 결석)
    private int week11, week12, week13, week14, week15, week16;                         // 출석 11~16주차
}

