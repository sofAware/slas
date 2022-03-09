package kr.sofaware.slas.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;             // 댓글 고유 번호

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;      // 작성자

    private String content;     // 내용
    private Date date;          // 작성일
}
