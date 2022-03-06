package kr.sofaware.slas.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@ToString
@Table(name = "lecture_video")
@IdClass(LectureVideoPK.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LectureVideo {
    @Id
    @ManyToOne
    @JoinColumn(name = "syllabus_id")
    private Syllabus syllabus;          // 강의

    @Id
    @Column
    private String id;                  // 강의영상 아이디 (<주차>-<회차>, 1-1, 1-2, ...)

    private String name;                // 강의영상 제목

    private Date attendanceStart;       // 출석 시작일
    private Date attendanceEnd;         // 출석 마감일

    private int duration;               // 강의영상 길이(분)

    private String attachmentPath;      // 강의영상 경로

    public void update(String id, String name, Date attendanceStart, Date attendanceEnd, int duration, String attachmentPath) {
        this.id = id;
        this.name = name;
        this.attendanceStart = attendanceStart;
        this.attendanceEnd = attendanceEnd;
        this.duration = duration;
        this.attachmentPath = attachmentPath;
    }
}