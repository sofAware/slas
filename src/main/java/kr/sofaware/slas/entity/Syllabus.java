package kr.sofaware.slas.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.DayOfWeek;
//import org.springframework.security.core.parameters.P;

@Data
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
    private String introduction;

    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Member professor;    // 담당교수

    /**
     * "21-1" 과 같은 년도, 학기 포맷을 "2021학년도 1학기"로 바꿔줌
     * @param yearSemester 년도학기
     * @return "2021학년도 1학기"
     */
    public static String formatYearSemester(String yearSemester) {
        return String.format("20%s년도 %c학기", yearSemester.substring(0, 2), yearSemester.charAt(3));
    }

    /**
     * 영어로 포맷의 요일을 한글로 반환해줌
     * @author 양경호
     * @param dayOfWeek 요일
     * @return "일", "월", ...
     */
    public static String translateDayOfWeek(String dayOfWeek) {
        switch (dayOfWeek) {
            case "SUN": return "일";
            case "MON": return "월";
            case "TUE": return "화";
            case "WED": return "수";
            case "THU": return "목";
            case "FRI": return "금";
            case "SAT": return "토";
        }
        return "";
    }

    /**
     * "금123", "화4, 목3" 이런식으로 한글로 포맷팅해서 강의 시간을 반환해줌
     * @author 양경호
     * @return "금123" or "화4, 목3"
     */
    public String formatClassTime() {
        String result = translateDayOfWeek(dayOfWeek1) + time1;

        if (dayOfWeek2 == null || dayOfWeek2.isEmpty())
            return result;

        return result += ", " + translateDayOfWeek(dayOfWeek2) + time2;
    }

    public String formatDetailName() {
        return name + " (" + id + ") [" + formatClassTime() + "]";
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
