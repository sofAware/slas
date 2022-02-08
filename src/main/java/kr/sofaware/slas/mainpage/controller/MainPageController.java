package kr.sofaware.slas.mainpage.controller;

import kr.sofaware.slas.mainpage.dto.StudentDto;
import kr.sofaware.slas.entity.Lecture;
import kr.sofaware.slas.service.ProfessorService;
import kr.sofaware.slas.mainpage.service.StudentMainPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@RequiredArgsConstructor
@Controller
public class MainPageController {

        private final StudentMainPageService studentMainPageService;
        private final ProfessorService professorService;

        //학생 메인페이지
        @GetMapping("/student")
        public String student(Model model) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();       //현재 로그인한 사용자의 id 받아오기
            UserDetails user = (UserDetails) principal;
            String username=((UserDetails) principal).getUsername();

            StudentDto student=studentMainPageService.findById(username);               //현재 로그인한 학생 찾기

            model.addAttribute("studentName",student.getName());    //현재 로그인한 학생 이름 view 에 전달


            //년도, 학기를 바탕으로 이 학생이 해당 학기에 들은 강의들의 syllabus 받아오기
            List<Lecture> lectureList=studentMainPageService.findByIdAndYearSemester("2019010101","21-2");

            System.out.println("this semester lectures's syllabuses!!!!!!!!");
            lectureList.forEach(lecture -> System.out.println(lecture.getSyllabus().getId()));



            return "student-mainpage";
        }

        //교수 메인페이지
        @GetMapping("/professor")
        public String professor() { return "professor"; }
    }
