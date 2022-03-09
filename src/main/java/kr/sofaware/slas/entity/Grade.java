package kr.sofaware.slas.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@ToString
@IdClass(GradePK.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Grade {
    @Id
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Member student;            // 학생

    @Id
    private int year;                   // 년도

    @Id
    private int semester;               // 학기

    private String major;               // 학과
    private int ranking;                // 석차
    private double gradeAvg;            // 전체 평점
    private double majorGradeAvg;       // 전공 학점
    private int totalCredit;            // 신청 학점
}