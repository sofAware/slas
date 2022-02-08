package kr.sofaware.slas.mainpage.dto;

import kr.sofaware.slas.entity.Student;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentDto {
    private String id;          // 학번
    //private String password;    // 비밀번호
    private String name;        // 이름
    private String major;       // 전공
    private int semester;       // 학기
    private String auth;        // 역할

    public StudentDto(Student entity){                  // entity 로부터 dto 생성
        this.id=entity.getId();
        //this.password=entity.getPassword();
        this.name=entity.getName();
        this.major=entity.getMajor();
        this.semester=entity.getSemester();
        this.auth=entity.getAuth();
    }
}
