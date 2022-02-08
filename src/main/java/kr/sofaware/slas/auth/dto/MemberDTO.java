package kr.sofaware.slas.auth.dto;

import kr.sofaware.slas.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberDTO {
    private String id;          // 학번 또는 교번
    private String password;    // 비밀번호
    private String name;        // 이름
    private String major;       // 전공
    private Integer semester;       // 학기

    private String role;        // 권한

    public static MemberDTO from(Member member) {
        return new MemberDTO(
                member.getId(),
                member.getPassword(),
                member.getName(),
                member.getMajor(),
                member.getSemester(),
                member.getRole());
    }
}
