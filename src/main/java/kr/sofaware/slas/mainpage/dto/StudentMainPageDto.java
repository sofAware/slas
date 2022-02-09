package kr.sofaware.slas.mainpage.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class StudentMainPageDto {
    private String id;          // 학번
    private String name;        // 이름
    private int admissionYear;  // 입학 년도

    private int year;           // 선택한 년도
    private int semester;       // 선택한 학기

    private List<SyllabusDto> syllabusDtoList;

    @Builder
    public StudentMainPageDto(String id, String name, int admissionYear, int year, int semester, List<SyllabusDto> syllabusDtoList){
        this.id=id;
        this.name=name;
        this.admissionYear=admissionYear;
        this.year=year;
        this.semester=semester;
        this.syllabusDtoList=syllabusDtoList;
    }
}
