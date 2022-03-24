package kr.sofaware.slas.function.mainpage.dto;

import kr.sofaware.slas.entity.Syllabus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
public class SyllabusDtoForProf extends SyllabusDto {
    List<AssignmentDto> assignmentDtoList;                      // 이 과목의 남은 과제 List
    Map<Date,List<AssignmentDto>> urgentAssignments;            // 가장 빠른 마감기한에 해당하는 과제들 (같은 날에 마감인 과제들이 여러개일 경우 대비)


    public SyllabusDtoForProf(Syllabus entity){                        // entity 로부터 dto 생성
        super(entity);

        this.assignmentDtoList=new ArrayList<>();
    }

    public void setUrgentAssignments(List<AssignmentDto> assignmentDtoList){
        this.urgentAssignments=new TreeMap<>();

        this.urgentAssignments.put(assignmentDtoList.get(0).getSubmitEnd(),new ArrayList<>());

        assignmentDtoList.forEach(assignment -> {
            if(assignment.getSubmitEnd().equals(assignmentDtoList.get(0).getSubmitEnd())) {
                this.urgentAssignments.get(assignmentDtoList.get(0).getSubmitEnd()).add(assignment);
            }
        });
    }
}
