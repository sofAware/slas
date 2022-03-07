package kr.sofaware.slas.entity;

import lombok.*;

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

    public static final int CATEGORY_NOTICE = 1;            // 공지사항
    public static final int CATEGORY_LECTURE_FILE = 2;      // 강의자료
    public static final int CATEGORY_ASSIGNMENT_SUBMIT = 3; // 제출과제
    public static final int CATEGORY_QNA = 4;               // 질문

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

    public void setCategory(int category) {
        this.category = category;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public void increaseViewCount() {
        view++;
    }

    /**
     * 게시글 수정 때 사용할 업데이트 함수
     * @author 양경호
     * @param title 제목
     * @param content 내용
     * @param attachmentName 첨부파일 이름
     * @param attachmentPath 첨부파일 경로
     */
    public void update(String title, String content, String attachmentName, String attachmentPath) {
        this.title = title;
        this.content = content;
        this.attachmentName = attachmentName;
        this.attachmentPath = attachmentPath;
    }
}
