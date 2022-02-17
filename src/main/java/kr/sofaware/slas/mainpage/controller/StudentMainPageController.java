package kr.sofaware.slas.mainpage.controller;

//import kr.sofaware.slas.service.ProfessorService;
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
import java.util.Iterator;
import java.util.List;
import java.util.Date;

import static kr.sofaware.slas.mainpage.dto.StudentMainPageDto.TOTAL_DAYOFWEEK;
import static kr.sofaware.slas.mainpage.dto.StudentMainPageDto.TOTAL_PERIOD;


@RequiredArgsConstructor
@Controller
@RequestMapping("/s")
public class StudentMainPageController {

        private final StudentMainPageService studentMainPageService;

        //학생 메인페이지
        @RequestMapping(value="main", method = {RequestMethod.GET, RequestMethod.POST})
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

            List<ArrayList<CellDto>> cellDtoList=CellDto.createCellDtoList(syllabusDtoList);            // 시간표 출력 위한 cellDtoList 생성


            // ↓ ↓ ↓ syllabusDtoList 의 각각의 syllabus 들의 noticeDtoList 에 최신 공지 3개 정도 add
            // category 1 인거 나중에 디벨롭 코드 머지하면 Board 클래스에 전역변수 선언돼있는걸로 바꾸기 !!!!
            // board 테이블에서 ( category 는 1이고 && syllabus_id 는 syllabusDtoList.get(i).id ) 인 것들을 찾아서 등록일 빠른 순으로(datetime 내림차순) 정렬 => 위에서부터 레코드 3개만 가져오기 => noticeDtoList 로 결과 가져옴!!
            for(SyllabusDto s : syllabusDtoList) {
                s.setNoticeDtoList(studentMainPageService.findFirst3ByCategoryAndSyllabus_IdOrderByDateDesc(1, s.getId()));
            }


            // ↓ ↓ ↓  syllabusDtoList 의 각각의 syllabus 들의 assignmentDtoList 에 아직 제출하지 않은 과제들을 제출 마감일 빠른 순으로 출력 => 최대 얼만큼까지 출력해줄지는 프론트에서 처리
            for(SyllabusDto s : syllabusDtoList) {
                List<AssignmentDto> assignmentDtoList = studentMainPageService.findBySyllabus_IdSubmitEndAfterOrderBySubmitEndAsc(s.getId(),new Date());

                for(Iterator<AssignmentDto> iter = assignmentDtoList.iterator(); iter.hasNext();){
                    AssignmentDto a=iter.next();
                    if(studentMainPageService.existsByAssignment_IdAndMember_Id(a.getId(),username))
                        iter.remove();
                }

                s.setAssignmentDtoList(assignmentDtoList);
            }


            // ↓ ↓ ↓  syllabusDtoList 의 각각의 syllabus 들의 videoLectureDtoList 에 아직 수강하지 않은 강의들을 마감일 빠른 순으로 (최대 5개? 3개? 까지) 출력
            // 이거는 근데 올라온 수업 강의 영상들을 시청했냐 안했냐 가 먼저 판단되야 해서 ***나중에*** 해야 할듯..
            // 근데 강의 시청했냐 아니냐를 어떻게 알지? 출석체크로 아나?? 출석->수강완료 / 지각->수강완료 / 결석->수강미완료 이렇게 처리?? 강의 몇퍼센트 시청했고 이런 것도 할 건가???!
            // 강의 영상 시청 여부를 board 의 칼럼으로 추가하는 방안은 ?!?!?!????!?

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
}
