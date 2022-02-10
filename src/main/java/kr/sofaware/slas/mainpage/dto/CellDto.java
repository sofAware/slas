package kr.sofaware.slas.mainpage.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CellDto {
    private String lectureName=null;
    private int cellSpan=1;

    @Builder
    public CellDto(String lectureName, int cellSpan){
        this.lectureName=lectureName;
        this.cellSpan=cellSpan;
    }
}
