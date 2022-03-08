package kr.sofaware.slas.auth;

import kr.sofaware.slas.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberDto {
    private String id;          // 학번 또는 교번
    private String password;    // 비밀번호
    private String name;        // 이름
    private String major;       // 전공
    private Integer semester;       // 학기

    private String role;        // 권한
}
