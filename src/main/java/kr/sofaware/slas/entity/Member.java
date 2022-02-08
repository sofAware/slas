package kr.sofaware.slas.entity;

import kr.sofaware.slas.auth.dto.MemberDTO;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member implements UserDetails {
    public static String ROLE_STUDENT = "ROLE_STUDENT";
    public static String ROLE_PROFESSOR = "ROLE_PROFESSOR";

    @Id
    private String id;          // 학번 또는 교번
    private String password;    // 비밀번호
    private String name;        // 이름
    private String major;       // 전공
    private int semester;       // 학기

    private String role;        // 역할

    public static Member from(MemberDTO memberDTO) {
        return new Member(
                memberDTO.getId(),
                memberDTO.getPassword(),
                memberDTO.getName(),
                memberDTO.getMajor(),
                memberDTO.getSemester(),
                memberDTO.getRole());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> roles = new HashSet<>();
        roles.add(new SimpleGrantedAuthority(role));
        return roles;
    }

    @Override
    public String getUsername() {
        return id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; //만료되지 않음
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; //잠기지 않음
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; //만료되지 않음
    }

    @Override
    public boolean isEnabled() {
        return true; //활성화 됨
    }
}
