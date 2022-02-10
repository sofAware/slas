package kr.sofaware.slas.mainpage.controller;

import kr.sofaware.slas.entity.Lecture;
import kr.sofaware.slas.entity.Member;
//import kr.sofaware.slas.service.ProfessorService;
import kr.sofaware.slas.entity.Syllabus;
import kr.sofaware.slas.mainpage.dto.*;
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

import static kr.sofaware.slas.mainpage.dto.StudentMainPageDto.TOTAL_DAYOFWEEK;
import static kr.sofaware.slas.mainpage.dto.StudentMainPageDto.TOTAL_PERIOD;


@RequiredArgsConstructor
@Controller
public class MainPageController {

        private final StudentMainPageService studentMainPageService;

        //학생 메인페이지
        @RequestMapping(value="/student", method = {RequestMethod.GET, RequestMethod.POST})
        public String student(Model model, @RequestParam("year") @Nullable Integer year, @RequestParam("semester") @Nullable Integer semester){
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();       //현재 로그인한 사용자의 id 받아오기
            UserDetails user = (UserDetails) principal;
            String username=((UserDetails) principal).getUsername();

            StudentDto student=studentMainPageService.findById(username);                                   //현재 로그인한 학생 찾기

            if((year == null) && (semester == null)) {                                                                                    //제일 처음 로딩 시 -> 현재 년도-학기 로
                year=LocalDate.now().getYear();
                if(1<=LocalDate.now().getMonthValue()&&LocalDate.now().getMonthValue()<=6)                              //1~6 월까지는 1학기 현재년도-1학기 시간표로 표시 & 7~12 월까지는 현재년도-2학기 시간표로 표시
                    semester=1;
                else
                    semester=2;
            }

            List<SyllabusDto> syllabusDtoList = studentMainPageService.findByIdAndYearSemester(username,year,semester);      //년도, 학기를 바탕으로 이 학생이 해당 학기에 들은 강의들의 syllabus 들을 받아와서 view 에 전달하기 위한 syllabusDto 들로 변환

            // 여기 진심 너무 지저분ㅠㅠㅠㅠ.... 함수로 빼고 싶은데 REPOSITORY 에 접근하는 함수는 아니라서 서비스에 빼기도 뭐하고 컨트롤러에 따로 함수로 빼기도 뭐하고... 어카지..
            // view 에 넘겨주기 위한 cellDto 들의 list 생성하는 부분..인데 어따가 넣아야 돼 ~!~!~!~!~!~!~!
            // 함수로 따로 빼고 로그 출력하는 부분들 다 지우기 !!!!!
            List<ArrayList<CellDto>> cellDtoList=new ArrayList<>(TOTAL_PERIOD);
            for(int i=0;i<TOTAL_PERIOD;i++)
                cellDtoList.add(new ArrayList<CellDto>(TOTAL_DAYOFWEEK));

            if(!syllabusDtoList.isEmpty()) {
                for (int i = 0; i < TOTAL_PERIOD; i++) {
                    for (int j = 0; j < TOTAL_DAYOFWEEK; j++) {
                        cellDtoList.get(i).add(new CellDto());
                    }
                }

                for (SyllabusDto s : syllabusDtoList) {
                    switch (s.getDayOfWeek1()) {
                        case "MON":
                            cellDtoList.get(Integer.parseInt(s.getTime1()) - 1).get(0).setLectureName(s.getName());
                            break;
                        case "TUE":
                            cellDtoList.get(Integer.parseInt(s.getTime1()) - 1).get(1).setLectureName(s.getName());
                            break;
                        case "WED":
                            cellDtoList.get(Integer.parseInt(s.getTime1()) - 1).get(2).setLectureName(s.getName());
                            break;
                        case "THU":
                            cellDtoList.get(Integer.parseInt(s.getTime1()) - 1).get(3).setLectureName(s.getName());
                            break;
                        case "FRI":
                            cellDtoList.get(Integer.parseInt(s.getTime1()) - 1).get(4).setLectureName(s.getName());
                            break;
                    }
                    switch (s.getDayOfWeek2()) {
                        case "MON":
                            cellDtoList.get(Integer.parseInt(s.getTime2()) - 1).get(0).setLectureName(s.getName());
                            break;
                        case "TUE":
                            cellDtoList.get(Integer.parseInt(s.getTime2()) - 1).get(1).setLectureName(s.getName());
                            break;
                        case "WED":
                            cellDtoList.get(Integer.parseInt(s.getTime2()) - 1).get(2).setLectureName(s.getName());
                            break;
                        case "THU":
                            cellDtoList.get(Integer.parseInt(s.getTime2()) - 1).get(3).setLectureName(s.getName());
                            break;
                        case "FRI":
                            cellDtoList.get(Integer.parseInt(s.getTime2()) - 1).get(4).setLectureName(s.getName());
                            break;
                    }
                }

                System.out.println("================= span 전 cellDtoList 들의 값은 ??? ");
                for(int i=0;i<TOTAL_PERIOD;i++){
                    for(CellDto currentCell : cellDtoList.get(i)){
                        System.out.println("lecture name : "+currentCell.getLectureName()+" span : "+currentCell.getCellSpan());
                    }
                    System.out.println();
                }

                for (int i=0;i<TOTAL_PERIOD-1;i++){                             // span 처리
                    int j=0;
                    for(CellDto currentCell : cellDtoList.get(i)){
                        CellDto underCell=cellDtoList.get(i+1).get(j);
                        if ((currentCell.getLectureName() != null) && (underCell.getLectureName() != null) && (currentCell.getLectureName().equals(underCell.getLectureName()))) {
                            currentCell.setCellSpan(2);
                            cellDtoList.get(i+1).remove(j);
                        }
                        j++;
                    }
                }

                System.out.println("================= span 후 cellDtoList 들의 값은 ??? ");
                for(int i=0;i<TOTAL_PERIOD;i++){
                    for(CellDto currentCell : cellDtoList.get(i)){
                        System.out.println("lecture name : "+currentCell.getLectureName()+" span : "+currentCell.getCellSpan());
                    }
                    System.out.println();
                }
            }
            // 여기까지 좀 함수로 따로 빼기...




            model.addAttribute("studentMainPageDto", StudentMainPageDto.builder()                       // studentMainPageDto 를 view 에 전달
                                                                                    .id(username)
                                                                                    .name(student.getName())
                                                                                    .admissionYear(Integer.parseInt(username.substring(0,4)))
                                                                                    .year(year)
                                                                                    .semester(semester)
                                                                                    .noLectures(syllabusDtoList.isEmpty())
                                                                                    .syllabusDtoList(syllabusDtoList)
                                                                                    .cellDtoList(cellDtoList)
                                                                                    .build());
            return "mainpage/student-mainpage";
        }

        //교수 메인페이지
        @GetMapping("/professor")
        public String professor() { return "professor"; }
}
