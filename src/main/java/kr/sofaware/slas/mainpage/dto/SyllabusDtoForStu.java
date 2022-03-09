package kr.sofaware.slas.mainpage.dto;

import kr.sofaware.slas.entity.Syllabus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SyllabusDtoForStu extends SyllabusDto {
    List<NoticeDto> noticeDtoList;                              // 이 과목의 최신 공지 List  => "최신" 의 기준은 최근에 올라온 공지사항 최대 3개까지..?
    List<AssignmentDto> assignmentDtoList;                      // 이 과목의 남은 과제 List
    // ********이 과목의 남은 강의 List 도 있어야 함!******

    public SyllabusDtoForStu(Syllabus entity){                        // entity 로부터 dto 생성
        super(entity);

        this.noticeDtoList=new ArrayList<>();
        this.assignmentDtoList=new ArrayList<>();
    }
}
