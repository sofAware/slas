package kr.sofaware.slas.board.entity;

import kr.sofaware.slas.auth.entity.Member;
import kr.sofaware.slas.syllabus.entity.Syllabus;
import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Board implements Serializable {
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
