package kr.sofaware.slas.board.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class AssignmentDTO extends BoardDTO {
    // 과제 관련 정보
    private Date submitStart;           // 제출 시작일
    private Date submitEnd;             // 제출 마감일

    public AssignmentDTO(String syllabusId, String syllabusName, int id, int category, int view, Date date, String author, String title, String content, String attachmentName, String attachmentPath, Date submitStart, Date submitEnd) {
        super(syllabusId, syllabusName, id, category, view, date, author, title, content, attachmentName, attachmentPath);
        this.submitStart = submitStart;
        this.submitEnd = submitEnd;
    }
}
