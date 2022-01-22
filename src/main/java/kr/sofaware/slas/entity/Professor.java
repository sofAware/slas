package kr.sofaware.slas.entity;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
public class Professor {
    @Id private int id;         // 교수 학번
    
    private String password;    // 비밀번호
    private String name;        // 이름
    private String major;       // 전공
}
