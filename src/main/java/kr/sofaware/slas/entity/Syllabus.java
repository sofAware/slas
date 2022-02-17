package kr.sofaware.slas.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.parameters.P;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.DayOfWeek;

@Entity
@Getter
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

    public static String translateDayOfWeek(String dayOfWeek) {
        switch (dayOfWeek) {
            case "SUN": return "일";
            case "MON": return "월";
            case "THE": return "화";
            case "WED": return "수";
            case "THU": return "목";
            case "FRI": return "금";
            case "SAT": return "토";
        }
        return "";
    }
    /***
     * "금123", "화4, 목3" 이런식으로 한글로 포맷팅해서 강의 시간을 반환해줌
     * @return "금123" or "화4, 목3"
     */
    public String formatClassTime() {
        String result = translateDayOfWeek(dayOfWeek1) + time1;

        if (dayOfWeek2 == null || dayOfWeek2.isEmpty())
            return result;

        return result += ", " + translateDayOfWeek(dayOfWeek2) + time2;
    }
}
