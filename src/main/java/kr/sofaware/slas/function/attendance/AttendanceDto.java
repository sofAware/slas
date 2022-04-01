package kr.sofaware.slas.function.attendance;

import kr.sofaware.slas.entity.Attendance;
import kr.sofaware.slas.entity.Member;
import kr.sofaware.slas.entity.Syllabus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@AllArgsConstructor
public class AttendanceDto {
    public static final String SYMBOL_ATTEND = "O";           // 출석을 나타낼 기호
    public static final String SYMBOL_LATE = "L";             // 지각을 나타낼 기호
    public static final String SYMBOL_ABSENT = "X";           // 결석을 나타낼 기호
    public static final String SYMBOL_VACANCY = "A";          //공결을 나타낼 기호

    private List<String> weeks = new ArrayList<>();                     // 16 주차 동안의 출석 정보 리스트 - 출석 상태를 나타내는 기호들로 구성

    public AttendanceDto(Attendance entity) {
        for (int i = 0; i < Attendance.TOTAL_WEEKS; i++) {
            switch (entity.getWeeksByList().get(i)) {                                        // attendance entity 의 getWeeksByList 를 이용해 16주차까지의 출석 정보를 리스트로 받아옴. 필드별로 getWeek1, getWeek2 ... 이렇게 하는 대신 !
                case Attendance.ATTEND:
                    this.weeks.add(SYMBOL_ATTEND);
                    break;
                case Attendance.LATE:
                    this.weeks.add(SYMBOL_LATE);
                    break;
                case Attendance.ABSENT:
                    this.weeks.add(SYMBOL_ABSENT);
                    break;
                case Attendance.VACANCY:
                    this.weeks.add(SYMBOL_VACANCY);
                    break;
                default:
                    this.weeks.add(null);
                    break;
            }
        }
    }

    //
    private String student;        // 학생
    private String syllabus;      // 강의
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
}