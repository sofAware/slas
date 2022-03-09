package kr.sofaware.slas.function.attendance;

import kr.sofaware.slas.entity.Attendance;

import java.util.ArrayList;
import java.util.List;

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
}