package kr.sofaware.slas.entity;

import kr.sofaware.slas.auth.MemberDto;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@ToString
@NoArgsConstructor
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

    @Lob
    private Blob profile;       // 프로필 사진

    public InputStream getProfile() {
        try {
            return profile.getBinaryStream();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Member from(MemberDto memberDTO) {
        return new Member(
                memberDTO.getId(),
                memberDTO.getPassword(),
                memberDTO.getName(),
                memberDTO.getMajor(),
                memberDTO.getSemester(),
                memberDTO.getRole(),
                null);
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
