package kr.sofaware.slas.function.total.dto;


import kr.sofaware.slas.entity.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QnADto {
    String studentId;
    String title;

    public QnADto(Board entity){
        this.studentId=entity.getMember().getId();
        this.title=entity.getTitle();
    }
}
