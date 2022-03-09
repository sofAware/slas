package kr.sofaware.slas.function.mainpage.dto;

import kr.sofaware.slas.entity.Syllabus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SyllabusDtoForProf extends SyllabusDto {
    List<AssignmentDto> assignmentDtoList;                      // 이 과목의 남은 과제 List

    public SyllabusDtoForProf(Syllabus entity){                        // entity 로부터 dto 생성
        super(entity);

        this.assignmentDtoList=new ArrayList<>();
    }
}
