package kr.sofaware.slas.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Comment {
    @Id
    private int id;             // 댓글 고유 번호

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;      // 작성자

    private String content;     // 내용
    private Date date;
}
