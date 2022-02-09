package kr.sofaware.slas.mainpage.dto;

import kr.sofaware.slas.entity.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfessorDto {
    private String id;          // 학번
    private String name;        // 이름
    private String major;       // 전공
    private String auth;        // 역할

    public ProfessorDto(Member entity){                  // entity 로부터 dto 생성
        this.id=entity.getId();
        this.name=entity.getName();
        this.major=entity.getMajor();
        this.auth=entity.getRole();
    }
}
