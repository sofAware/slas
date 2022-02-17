package kr.sofaware.slas.mainpage.dto;

import kr.sofaware.slas.entity.Member;
import kr.sofaware.slas.entity.Syllabus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SyllabusDto {
    private String id;                                          // 학정번호

    private String name;                                        // 과목명
    private String category;                                    // 카테고리 (전선, 교필, 교선, ...)
    private int credit;                                         // 학점 (1, 2, 3, 4)

    private String dayOfWeek1;                                  // 요일1 (MON, TUE, ...)
    private String time1;                                       // 교시1 (1, 2, ...)
    private String dayOfWeek2;
    private String time2;

    private ProfessorDto professor;                             // 담당교수

    List<NoticeDto> noticeDtoList;                              // 이 과목의 최신 공지 List  => "최신" 의 기준은 최근에 올라온 공지사항 최대 3개까지..?
    List<AssignmentDto> assignmentDtoList;                      // 이 과목의 남은 과제 List
    // 이 과목의 남은 강의 List 도 있어야 함!

    public SyllabusDto(Syllabus entity){                        // entity 로부터 dto 생성
        this.id=entity.getId();
        this.name=entity.getName();
        this.category=entity.getCategory();
        this.credit=entity.getCredit();
        this.dayOfWeek1=entity.getDayOfWeek1();
        this.time1=entity.getTime1();
        this.dayOfWeek2=entity.getDayOfWeek2();
        this.time2=entity.getTime2();
        this.professor=new ProfessorDto(entity.getProfessor());

        this.noticeDtoList=new ArrayList<>();
        this.assignmentDtoList=new ArrayList<>();
    }
}
