package kr.sofaware.slas.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfessorDto {
    private String id;          // 교수 학번
    private String password;    // 비밀번호
    private String name;        // 이름
    private String major;       // 전공
    private String auth;        // 역할
}
