package kr.sofaware.slas.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@ToString
@Builder
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

    /**
     * 과제 수정 때 사용할 업데이트 함수
     * @author 양경호
     * @param name 제목
     * @param submitStart 제출시작일
     * @param submitEnd 제출마감일
     * @param content 내용
     * @param attachmentName 첨부파일 이름
     * @param attachmentPath 첨부파일 경로
     */
    public void update(String name, Date submitStart, Date submitEnd, String content, String attachmentName, String attachmentPath) {
        this.name = name;
        this.submitStart = submitStart;
        this.submitEnd = submitEnd;
        this.content = content;
        this.attachmentName = attachmentName;
        this.attachmentPath = attachmentPath;
    }
}
