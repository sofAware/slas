package kr.sofaware.slas.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


@NoArgsConstructor(access = AccessLevel.PROTECTED) // 불완전한 객체 생성을 막아주는 역할
@Entity
@Getter
public class Professor implements UserDetails {
    @Id
    private String id;         // 교수 학번

    private String password;    // 비밀번호
    private String name;        // 이름
    private String major;       // 전공
    private String auth;        // 역할

    @Builder
    public Professor(String id, String password, String name, String major, String auth){
        this.id = id;
        this.password = password;
        this.name = name;
        this.major = major;
        this.auth = auth;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> roles = new HashSet<>();
        roles.add(new SimpleGrantedAuthority(auth));
        return roles;
    }

    //Unique Value
    @Override
    public String getUsername() {
        return this.id;
    }

    @Override
    public String getPassword() {
        return this.password;
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
