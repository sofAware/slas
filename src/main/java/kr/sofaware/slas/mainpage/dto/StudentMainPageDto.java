package kr.sofaware.slas.mainpage.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class StudentMainPageDto {
    public static int TOTAL_PERIOD=7;                       // 교시는 7교시까지
    public static int TOTAL_DAYOFWEEK=5;                    // 요일은 월화수목금 이렇게 5개만

    private String id;                                      // 학번
    private String name;                                    // 이름
    private int admissionYear;                              // 입학 년도

    private int year;                                       // 선택한 년도
    private int semester;                                   // 선택한 학기
    private boolean noLectures;                             // 해당 년도-학기에 들은 강의가 없을 경우

    private List<SyllabusDto> syllabusDtoList;              // 이번 년도-학기에 들은 강의들의 syllabus 들의 list
    private List<ArrayList<CellDto>> cellDtoList;           // 시간표 출력을 위한 2차원 cell Dto 들의 list

    @Builder
    public StudentMainPageDto(String id, String name, int admissionYear, int year, int semester, boolean noLectures, List<SyllabusDto> syllabusDtoList, List<ArrayList<CellDto>> cellDtoList){
        this.id=id;
        this.name=name;
        this.admissionYear=admissionYear;

        this.year=year;
        this.semester=semester;
        this.noLectures=noLectures;

        this.syllabusDtoList=syllabusDtoList;
        this.cellDtoList=cellDtoList;
    }
}
