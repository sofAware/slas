package kr.sofaware.slas.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@ToString
@IdClass(AttendancePK.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class   Attendance {

    public static final int ATTEND = 1;             //출석
    public static final int LATE = 2;               //지각
    public static final int ABSENT = 3;             //결석
    public static final int VACANCY = 4;            //공결

    public static final int TOTAL_WEEKS=16;


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

    public List<Integer> getWeeksByList(){                                              // week 필드 하나하나 get으로 접근하기 너무 힘들어서 만들었어욤 - 지민 -
        List<Integer> weeksList=new ArrayList<>();

        weeksList.add(week1);
        weeksList.add(week2);
        weeksList.add(week3);
        weeksList.add(week4);
        weeksList.add(week5);
        weeksList.add(week6);
        weeksList.add(week7);
        weeksList.add(week8);
        weeksList.add(week9);
        weeksList.add(week10);
        weeksList.add(week11);
        weeksList.add(week12);
        weeksList.add(week13);
        weeksList.add(week14);
        weeksList.add(week15);
        weeksList.add(week16);

        return weeksList;
    }

    public static String formatWeek(int week){
        switch (week){
            case  1 : return "O"; // 출석
            case  2 : return "L"; // 지각
            case  3 : return "X"; // 결석
            case  4 : return "A"; // 공결
        }
        return "Z";
    }

}