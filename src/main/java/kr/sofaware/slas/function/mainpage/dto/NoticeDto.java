package kr.sofaware.slas.function.mainpage.dto;

import kr.sofaware.slas.entity.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class NoticeDto {
    private int id;                         // 게시글 id -> 메인 페이지에서 해당 공지 링크 눌렀을 때 공지사항 조회 API 에 id 전달해서 해당 id 에 대한 게시글 띄워주는 링크로 보내려고..
    private Date uploadDate;                // 공지 등록 날짜
    private String lectureName;             // 과목 이름
    private String title;                   // 공지 제목

    @Builder
    public NoticeDto(int id, Date uploadDate, String lectureName, String title){
        this.id=id;
        this.uploadDate=uploadDate;
        this.lectureName=lectureName;
        this.title=title;
    }

    public NoticeDto(Board entity){         // board entity 로부터 noticeDto 를 생성
        this.id=entity.getId();
        this.uploadDate=entity.getDate();
        this.lectureName=entity.getSyllabus().getName();
        this.title=entity.getTitle();
    }
}
