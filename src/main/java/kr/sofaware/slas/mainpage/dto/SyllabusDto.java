package kr.sofaware.slas.mainpage.dto;

import kr.sofaware.slas.entity.Syllabus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SyllabusDto {
    private String id;              // 학정번호

    private String name;            // 과목명
    private String category;        // 카테고리 (전선, 교필, 교선, ...)
    private int credit;             // 학점 (1, 2, 3, 4)

    private String dayOfWeek1;      // 요일1 (MON, TUE, ...)
    private String time1;           // 교시1 (1, 2, ...)
    private String dayOfWeek2;
    private String time2;

    private ProfessorDto professor;    // 담당교수

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
    }
}
