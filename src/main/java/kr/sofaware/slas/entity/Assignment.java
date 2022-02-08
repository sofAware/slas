package kr.sofaware.slas.entity;

import kr.sofaware.slas.entity.Syllabus;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;                 // 과제 고유 번호

    @ManyToOne
    @JoinColumn(name = "syllabus_id")
    private Syllabus syllabus;          // 강의

    private String name;                // 과제명

    @Column(length = 500)
    private String content;             // 과제 내용

    private Date submitStart;           // 제출 시작일
    private Date submitEnd;             // 제출 마감일

    private String attachmentName;      // 과제 자료 이름
    private String attachmentPath;      // 과제 자료 경로
}
