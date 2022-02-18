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
public class ProfessorMainPageDto {
    private String id;                                      // 학번
    private String name;                                    // 이름

    private boolean noLectures;                             // 해당 년도-학기에 수업한 강의가 없을 경우

    private List<SyllabusDtoForProf> syllabusDtoList;              // 이번 년도-학기에 수업한 강의들의 syllabus 들의 list
    private List<ArrayList<CellDto>> cellDtoList;           // 시간표 출력을 위한 2차원 cell Dto 들의 list

    @Builder
    public ProfessorMainPageDto(String id, String name, boolean noLectures, List<SyllabusDtoForProf> syllabusDtoList, List<ArrayList<CellDto>> cellDtoList){
        this.id=id;
        this.name=name;

        this.noLectures=noLectures;

        this.syllabusDtoList=syllabusDtoList;
        this.cellDtoList=cellDtoList;
    }
}
