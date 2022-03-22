package kr.sofaware.slas.auth;

import kr.sofaware.slas.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final MemberService memberService;

    // 인가 거부 핸들링 필드
    private AccessDeniedHandler accessDeniedHandler = (request, response, accessDeniedException) -> {
        response.sendError(HttpStatus.FORBIDDEN.value());
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 기본 인가 범위
                .authorizeRequests()
                .antMatchers("/login", "/signup", "/assets/**", "/api/**").permitAll()  // 누구나 접근 가능
                .antMatchers("/p/**").hasRole("PROFESSOR")                              // 교수만 접근 허용
                .antMatchers("/s/**").hasRole("STUDENT")                                // 학생만 접근 허용
                .anyRequest().authenticated()                                                      // 나머지는 로그인만 하면 접근 가능

                // 로그인
                .and()
                .formLogin() //로그인에 대한 설정
                .loginPage("/login")// 로그인 페이지 링크
                .loginProcessingUrl("/login")//로그인 HTML에서 SUBMIT POST 요청시
                .defaultSuccessUrl("/")//로그인 성공시 연결되는 주소

                // 로그아웃
                .and()
                .logout() // 로그아웃 관련 설정
                .logoutSuccessUrl("/login") // 로그아웃 성공시 연결되는 주소
                .invalidateHttpSession(true) // 로그아웃시 저장해 둔 세션 날리기

                // csrf 일부 비활성화
                .and()
                .csrf() //회원가입은 /login 에서 이뤄지므로 /signup 으로 post 날라오면
                .ignoringAntMatchers("/signup", "/api/**") // csrf 비활성화

                // 인증 및 실패 핸들링
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
        ;
    }

    @Override
    // 로그인 시 필요한 정보를 가져오기
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService) // 맴버 정보는 userService 에서 가져온다
                .passwordEncoder(new BCryptPasswordEncoder()); // 패스워드 인코더는 passwordEncoder(BCrypt 사용)
    }
}
