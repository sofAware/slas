package kr.sofaware.slas.security;

import kr.sofaware.slas.service.ProfessorService;
import kr.sofaware.slas.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration // Bean 관리하는 어노테이션
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final StudentService studentService;
    private final ProfessorService professorService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/home", "/", "/login", "/signup").permitAll() //누구나 접근 가능
                    .antMatchers("/student").hasRole("STUDENT") //학생만 접근 가능
                    .antMatchers("/professor").hasRole("PROFESSOR") //교수만 접근 가능
                    .antMatchers("/s/**").hasRole("STUDENT")
                    .antMatchers("/p/**").hasRole("PROFESSOR")
                    .anyRequest().authenticated() //나머지는 권한이 있기만 하면 접근 가능
                .and()
                    .formLogin() //로그인에 대한 설정
                    .loginPage("/login")// 로그인 페이지 링크
                    .loginProcessingUrl("/login_proc")//로그인 HTML에서 SUBMIT POST 요청시
                    .defaultSuccessUrl("/home")//로그인 성공시 연결되는 주소
                .and()
                    .logout() // 로그아웃 관련 설정
                    .logoutSuccessUrl("/login") // 로그아웃 성공시 연결되는 주소
                    .invalidateHttpSession(true) // 로그아웃시 저장해 둔 세션 날리기
        ;
    }

    @Override
    public void configure(WebSecurity web) {
        // /css/**, /images/**, /js/** 등 정적 리소스는 보안필터를 거치지 않게 한다.
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Override
    // 로그인 시 필요한 정보를 가져오기
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(studentService) // 학생 정보는 userService 에서 가져온다
                .passwordEncoder(new BCryptPasswordEncoder()); // 패스워드 인코더는 passwordEncoder(BCrypt 사용)

        auth.userDetailsService(professorService) //교수
                .passwordEncoder(new BCryptPasswordEncoder());
    }
}
