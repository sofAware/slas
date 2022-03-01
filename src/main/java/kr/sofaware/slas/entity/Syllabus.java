package kr.sofaware.slas.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Syllabus {
    @Id
    private String id;              // 학정번호 (21-1-0201-1-0001-01)

    private String name;            // 과목명
    private String category;        // 카테고리 (전선, 교필, 교선, ...)
    private int credit;             // 학점 (1, 2, 3, 4)

    private String dayOfWeek1;      // 요일1 (MON, TUE, ...)
    private String time1;           // 교시1 (1, 2, ...)
    private String dayOfWeek2;
    private String time2;

    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Member professor;    // 담당교수

    public static String formatYearSemester(String yearSemester) {
        return String.format("20%s년도 %c학기", yearSemester.substring(0, 2), yearSemester.charAt(3));
    }

    public static String formatWeek(String week) {
        return String.format("%c주차",  week.charAt(3));
    }

    public static int formatDayToInt(String dayOfWeek){
        if("MON".equals(dayOfWeek))
            return 0;
        else if("TUE".equals(dayOfWeek))
            return 1;
        else if("WED".equals(dayOfWeek))
            return 2;
        else if("THU".equals(dayOfWeek))
            return 3;
        else if("FRI".equals(dayOfWeek))
            return 4;
        else if("SAT".equals(dayOfWeek))
            return 5;
        else
            return -1;
    }
}
