package kr.sofaware.slas.auth;

import kr.sofaware.slas.service.LectureService;
import kr.sofaware.slas.service.SyllabusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class FileFilter extends OncePerRequestFilter {

    private final LectureService lectureService;
    private final SyllabusService syllabusService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 파일쪽 URL인지 체크
        if (!request.getRequestURI().startsWith("/upload/**")) {
            filterChain.doFilter(request, response);
            return;
        }

        // URL 분리
        String[] split = request.getRequestURI().split("/");

        // 지정된 파일이 아닌 상위로 접근하려 하는 경우 잘못된 요청 반환
        if (split.length < 4) {
            response.setStatus(400);
            return;
        }

        String username = request.getUserPrincipal().getName();

        // 카테고리 분류 (syllabus, ...)
        boolean auth = false;

        switch(split[2]) {
            case "syllabus":
                String syllabusId = split[3];

                if (request.isUserInRole("STUDENT")) {
                    auth = lectureService.existsBySyllabus_IdAndStudent_Id(syllabusId, username);
                } else if (request.isUserInRole("PROFESSOR")) {
                    auth = syllabusService.existsByIdAndProfessor_Id(syllabusId, username);
                }
                break;
        }

        // 인가 받았다면 요청 허용
        if (auth)
            filterChain.doFilter(request, response);
        // 인가받지 못했다면 요청 거부!
        else {
            response.setStatus(403);
            return;
        }
    }
}
