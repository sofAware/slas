package kr.sofaware.slas.function.total.dto;

import kr.sofaware.slas.entity.Board;
import kr.sofaware.slas.entity.LectureVideo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class LectureVideoDto {
    private int id;                     // 강의영상 아이디 (<주차>-<회차>, 1-1, 1-2, ...)

    private String name;                // 강의영상 제목

    private Date attendanceStart;       // 출석 시작일
    private Date attendanceEnd;         // 출석 마감일

    private int duration;               // 강의영상 길이(분)

    public LectureVideoDto(LectureVideo entity){
        this.id=entity.getId();
        this.name=entity.getName();
        this.attendanceStart=entity.getAttendanceStart();
        this.attendanceEnd=entity.getAttendanceEnd();
        this.duration=entity.getDuration();
    }
}
