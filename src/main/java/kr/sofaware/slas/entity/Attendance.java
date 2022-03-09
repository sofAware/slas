package kr.sofaware.slas.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString
@IdClass(AttendancePK.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Attendance {
    public static final int ATTEND = 1;             // 출석
    public static final int LATE = 2;               // 강의자료
    public static final int ABSENT = 3;             // 강의영상

    public static final int TOTAL_WEEKS=16;         // 총 16주차까지 존재

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

    /**
     * @author 정지민
     * @return
     */
    public List<Integer> getWeeksByList(){                                              // week 필드 하나하나 get으로 접근하기 너무 힘들어서 만들었어욤 - 지민 -
        List<Integer> weeksList=new ArrayList<>();

        weeksList.add(this.week1);
        weeksList.add(this.week2);
        weeksList.add(this.week3);
        weeksList.add(this.week4);
        weeksList.add(this.week5);
        weeksList.add(this.week6);
        weeksList.add(this.week7);
        weeksList.add(this.week8);
        weeksList.add(this.week9);
        weeksList.add(this.week10);
        weeksList.add(this.week11);
        weeksList.add(this.week12);
        weeksList.add(this.week13);
        weeksList.add(this.week14);
        weeksList.add(this.week15);
        weeksList.add(this.week16);

        return weeksList;
    }
}

