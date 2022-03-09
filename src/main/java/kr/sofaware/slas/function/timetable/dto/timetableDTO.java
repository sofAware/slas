package kr.sofaware.slas.function.timetable.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class timetableDTO {
    private String className;
    private String professorName;

    public timetableDTO() {
        className = "";
        professorName = "";
    }
    public timetableDTO(String className, String professorName) {
        this.className = className;
        this.professorName = professorName;
    }
}
