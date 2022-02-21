package kr.sofaware.slas.entity;

import lombok.*;
import org.thymeleaf.util.DateUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Board implements Serializable {
    public static final int CATEGORY_NOTICE = 1;       // 공지사항
    public static final int CATEGORY_FILES = 2;        // 강의자료
    public static final int CATEGORY_VIDEO = 3;        // 강의영상
    public static final int CATEGORY_ASSIGNMENT = 4;   // 과제
    public static final int CATEGORY_QNA = 5;          // 질문

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;                 // 글 고유 번호

    @ManyToOne
    @JoinColumn(name = "syllabus_id")
    private Syllabus syllabus;          // 강의

    private int category;               // 분류 (1: 공지사항, 2: 강의자료, ...)
    private String title;               // 제목

    @Column(length = 500)
    private String content;             // 내용

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;              // 작성자

    private Date date;                  // 등록일
    private int view;                   // 조회수

    @ManyToOne
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;          // 과제 번호

    private String attachmentName;      // 첨부 파일 이름
    private String attachmentPath;      // 첨부 파일 경로
}
