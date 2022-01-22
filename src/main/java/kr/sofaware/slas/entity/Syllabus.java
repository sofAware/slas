package kr.sofaware.slas.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Syllabus {
    @Id
    private String id;              // 학정번호

    private String name;            // 과목명
    private int year;               // 년도
    private int semester;           // 학기 (1~2)
    private String category;        // 카테고리 (전선, 교필, 교선, ...)
    private int credit;             // 학점 (1, 2, 3, 4)

    private String dayOfWeek1;      // 요일1 (MON, TUE, ...)
    private String time1;           // 교시1 (1, 2, ...)
    private String dayOfWeek2;
    private String time2;

    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Professor professor;    // 담당교수
}
