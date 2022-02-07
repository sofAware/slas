package kr.sofaware.slas.dto;

import kr.sofaware.slas.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class BoardDTO {
    // 관련 과목 정보
    private String syllabusId;        // 학정 번호
    private String syllabusName;      // 강의 제목

    // 게시글 정보
    private int id;           // 글 번호
    private int category;     // 분류
    private int view;         // 조회수
    private Date date;        // 등록일
    private String author;    // 작성자

    private String title;     // 제목
    private String content;   // 내용

    private String attachmentName;      // 첨부 파일 이름
    private String attachmentPath;      // 첨부 파일 경로

    public static BoardDTO from(Board board) {
        return new BoardDTO(
                board.getSyllabus().getId(),
                board.getSyllabus().getName(),
                board.getId(),
                board.getCategory(),
                board.getView(),
                board.getDate(),
                board.getProfessor() == null ?
                        board.getStudent().getName() :
                        board.getProfessor().getName(),
                board.getTitle(),
                board.getContent(),
                board.getAttachmentName(),
                board.getAttachmentPath()
                );
    }
}
