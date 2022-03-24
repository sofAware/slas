package kr.sofaware.slas.function.mainpage.dto;

import kr.sofaware.slas.entity.Assignment;
import kr.sofaware.slas.entity.Syllabus;
import kr.sofaware.slas.function.total.dto.LectureVideoDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
public class SyllabusDtoForStu extends SyllabusDto {
    List<NoticeDto> noticeDtoList;                              // 이 과목의 최신 공지 List  => "최신" 의 기준은 최근에 올라온 공지사항 최대 3개까지..?

    List<AssignmentDto> assignmentDtoList;                      // 이 과목의 남은 과제 List
    Map<Date,List<AssignmentDto>> urgentAssignments;            // 가장 빠른 마감기한에 해당하는 과제들 (같은 날에 마감인 과제들이 여러개일 경우 대비)

    List<LectureVideoDto> lectureVideoDtoList;                  // 이 과목의 남은 강의 List
    Map<Date,List<LectureVideoDto>> urgentLectureVideos;        // 가장 빠른 마감기한에 해당하는 강의들 (같은 날에 마감인 강의들이 여러개일 경우 대비)

    public SyllabusDtoForStu(Syllabus entity){                        // entity 로부터 dto 생성
        super(entity);

        this.noticeDtoList=new ArrayList<>();
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

    public void setUrgentLectureVideos(List<LectureVideoDto> lectureVideoDtoList){
        this.urgentLectureVideos=new TreeMap<>();

        this.urgentLectureVideos.put(lectureVideoDtoList.get(0).getAttendanceEnd(), new ArrayList<>());

        lectureVideoDtoList.forEach(lv -> {
            if(lv.getAttendanceEnd().equals(lectureVideoDtoList.get(0).getAttendanceEnd())){
                this.urgentLectureVideos.get(lectureVideoDtoList.get(0).getAttendanceEnd()).add(lv);
            }
        });
    }
}
