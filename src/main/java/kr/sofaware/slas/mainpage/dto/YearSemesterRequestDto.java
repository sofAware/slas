package kr.sofaware.slas.mainpage.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class YearSemesterRequestDto {
    private int year;
    private int semester;

    @Builder
    public YearSemesterRequestDto(int year, int semester){
        this.year=year;
        this.semester=semester;
    }
}
