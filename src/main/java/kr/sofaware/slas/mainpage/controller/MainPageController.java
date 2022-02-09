package kr.sofaware.slas.mainpage.controller;

import kr.sofaware.slas.entity.Lecture;
import kr.sofaware.slas.entity.Member;
//import kr.sofaware.slas.service.ProfessorService;
import kr.sofaware.slas.entity.Syllabus;
import kr.sofaware.slas.mainpage.dto.StudentDto;
import kr.sofaware.slas.mainpage.dto.StudentMainPageDto;
import kr.sofaware.slas.mainpage.dto.SyllabusDto;
import kr.sofaware.slas.mainpage.dto.YearSemesterRequestDto;
import kr.sofaware.slas.mainpage.service.StudentMainPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Controller
public class MainPageController {

        private final StudentMainPageService studentMainPageService;

        //학생 메인페이지
        @RequestMapping(value="/student", method = {RequestMethod.GET, RequestMethod.POST})
        public String student(Model model, @RequestBody @Nullable YearSemesterRequestDto requestDto){
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();       //현재 로그인한 사용자의 id 받아오기
            UserDetails user = (UserDetails) principal;
            String username=((UserDetails) principal).getUsername();

            StudentDto student=studentMainPageService.findById(username);                                   //현재 로그인한 학생 찾기

            List<SyllabusDto> syllabusDtoList=new ArrayList<>();          //년도, 학기를 바탕으로 이 학생이 해당 학기에 들은 강의들의 syllabus 들을 받아와서 view 에 전달하기 위한 syllabusDto 들로 변환

            if(requestDto == null) {                                                                                    //제일 처음 로딩 시 -> 현재 년도-학기 로
                int semester;
                if(1<=LocalDate.now().getMonthValue()&&LocalDate.now().getMonthValue()<=6)                              //1~6 월까지는 1학기 현재년도-1학기 시간표로 표시 & 7~12 월까지는 현재년도-2학기 시간표로 표시
                    semester=1;
                else
                    semester=2;

                syllabusDtoList = studentMainPageService.findByIdAndYearSemester(username,LocalDate.now().getYear(),semester);
            }
            else{                                                                                                        //selectBox 로 요청 시 -> 요청받은 년도-학기 로
                syllabusDtoList = studentMainPageService.findByIdAndYearSemester(username,requestDto.getYear(),requestDto.getSemester());
            }

            model.addAttribute("studentMainPageDto", StudentMainPageDto.builder()                       // studentMainPageDto 를 view 에 전달
                                                                                    .id(username)
                                                                                    .name(student.getName())
                                                                                    .admissionYear(Integer.parseInt(username.substring(0,4)))
                                                                                    .year(LocalDate.now().getYear())
                                                                                    .semester(1)
                                                                                    .syllabusDtoList(syllabusDtoList)
                                                                                    .build());

            return "mainpage/student-mainpage";
        }

        //교수 메인페이지
        @GetMapping("/professor")
        public String professor() { return "professor"; }
    }
