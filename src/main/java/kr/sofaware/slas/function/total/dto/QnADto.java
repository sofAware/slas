package kr.sofaware.slas.function.total.dto;


import kr.sofaware.slas.entity.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class QnADto {
    String studentId;
    String studentName;
    String title;
    Date date;

    public QnADto(Board entity){
        this.studentId=entity.getMember().getId();
        this.studentName=entity.getMember().getName();
        this.title=entity.getTitle();
        this.date=entity.getDate();
    }
}
