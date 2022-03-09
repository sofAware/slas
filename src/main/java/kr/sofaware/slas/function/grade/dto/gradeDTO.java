package kr.sofaware.slas.function.grade.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class gradeDTO {
    private String syllabusId;
    private String syllabusName;
    private String major;
    private String category;
    private int credit;
    private String grade;

    public gradeDTO (String syllabusId, String syllabusName, String major, String category, int credit, double grade)
    {
        this.syllabusId = syllabusId;
        this.syllabusName = syllabusName;
        this.major = major;
        this.category = category;
        this.credit = credit;
        this.grade = gradeNumToChar(grade);
    }

    public String gradeNumToChar(double gradeNum)
    {
        if(gradeNum == 4.5)
            return "A+";
        else if(gradeNum == 4.0)
            return "A0";
        else if(gradeNum == 3.5)
            return "B+";
        else if(gradeNum == 3.0)
            return "B0";
        else if(gradeNum == 2.5)
            return "C+";
        else if(gradeNum == 2.0)
            return "C0";
        else if(gradeNum == 1.5)
            return "D+";
        else if(gradeNum == 1.0)
            return "D0";
        else
            return "F";
    }
}

