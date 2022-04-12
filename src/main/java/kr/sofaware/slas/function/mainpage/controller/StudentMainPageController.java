package kr.sofaware.slas.function.mainpage.controller;

//import kr.sofaware.slas.service.ProfessorService;
import kr.sofaware.slas.entity.*;
import kr.sofaware.slas.function.mainpage.dto.*;
import kr.sofaware.slas.function.total.dto.LectureVideoDto;
import kr.sofaware.slas.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RequiredArgsConstructor
@Controller
@RequestMapping("/s")
public class StudentMainPageController {

        private final AssignmentService assignmentService;
        private final LectureService lectureService;
        private final MemberService memberService;
        private final NoticeService noticeService;
        private final LectureVideoService lectureVideoService;
        private final AttendanceService attendanceService;

        //학생 메인페이지
        @GetMapping("main")
        public String studentMain(Model model, @RequestParam("year-semester") @Nullable String yearSemester){
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();       //현재 로그인한 사용자의 id 받아오기
            UserDetails user = (UserDetails) principal;
            String username=((UserDetails) principal).getUsername();

            Member student=memberService.loadUserByUsername(username);                                   //현재 로그인한 학생 찾기

            // 사용자의 수강 학기와 과목들 조회
            Map<String, List<Syllabus>> lectures = lectureService.mapAllByStudentId(username);

            if (yearSemester == null) {
                ArrayList<String> yearSemesters = new ArrayList<>(lectures.keySet());
                // 이 사람이 들었던 수업이 없을 경우 그냥 리턴
                if (yearSemesters.isEmpty())
                    return "mainpage/student-mainpage";

                yearSemester = yearSemesters.get(0);
            }

            Map<String, String> formatYS = new TreeMap<>(Collections.reverseOrder());
            lectures.keySet().forEach(s -> formatYS.put(s, Syllabus.formatYearSemester(s)));

            // 학기 선택 리스트
            model.addAttribute("mapYS", formatYS);
            model.addAttribute("yearSemester", Syllabus.formatYearSemester(yearSemester));
            model.addAttribute("formatYS", Syllabus.formatYearSemester(yearSemester));

            List<SyllabusDtoForStu> syllabusDtoList = new ArrayList<>();                            //년도, 학기를 바탕으로 이 학생이 해당 학기에 들은 강의들의 syllabus 들을 받아와서 view 에 전달하기 위한 syllabusDto 들로 변환
            for(Syllabus s : lectures.get(yearSemester)){
                syllabusDtoList.add(new SyllabusDtoForStu(s));
            }

            List<SyllabusDto> listForCreatingCellDtoList=new ArrayList<>();                                        // List<SyllabusDto> 에 List<SyllabusDtoForStu> 를 넣어줌.. 둘은 상속 관계.. 이 방법밖에 없나.. 마음에 안 드렁 ㅠㅠㅠ 왜 바로 안 들어가는 거 ㅜㅜ
            for(SyllabusDtoForStu s : syllabusDtoList)
                listForCreatingCellDtoList.add(s);

            List<ArrayList<CellDto>> cellDtoList=CellDto.createCellDtoList(listForCreatingCellDtoList);            // 시간표 출력 위한 cellDtoList 생성

            // ↓ ↓ ↓ syllabusDtoList 의 각각의 syllabus 들의 noticeDtoList 에 최신 공지 3개 정도 add
            // board 테이블에서 ( 공지사항이고 && syllabus_id 는 syllabusDtoList.get(i).id ) 인 것들을 찾아서 등록일 빠른 순으로(datetime 내림차순) 정렬 => 위에서부터 레코드 3개만 가져오기 => noticeDtoList 로 결과 가져옴!!
            for(SyllabusDtoForStu s : syllabusDtoList) {
                s.setNoticeDtoList(noticeService.findFirst3ByCategoryAndSyllabus_IdOrderByDateDesc(s.getId()));
            }


            // ↓ ↓ ↓  syllabusDtoList 의 각각의 syllabus 들의 assignmentDtoList 에 아직 제출하지 않은 과제들을 제출 마감일 빠른 순으로 출력 => 최대 얼만큼까지 출력해줄지는 프론트에서 처리
            for(SyllabusDtoForStu s : syllabusDtoList) {
                List<Assignment> assignmentList = assignmentService.findBySyllabus_IdSubmitEndAfterOrderBySubmitEndAsc(s.getId(),new Date());

                for(Iterator<Assignment> iter = assignmentList.iterator(); iter.hasNext();){                      // 제출한 과제라면 목록에서 삭제
                    Assignment a=iter.next();
                    if(assignmentService.existsByAssignment_IdAndMember_Id(a.getId(),username))
                        iter.remove();
                }

                List<AssignmentDto> assignmentDtoList=new ArrayList<>();                                        // dto 로 변환해서 syllabusDto 내부에 set
                assignmentList.forEach(assignment -> assignmentDtoList.add(new AssignmentDto(assignment)));
                s.setAssignmentDtoList(assignmentDtoList);

                // 이 과목에서 제출 마감일 젤 빠른 과제가 여러 개일 경우 처리
                if(assignmentDtoList.isEmpty()==false)
                    s.setUrgentAssignments(assignmentDtoList);
            }

            // ↓ ↓ ↓  syllabusDtoList 의 각각의 syllabus 들의 lectureVideoDtoList 에 아직 수강하지 않은 강의들을 마감일 빠른 순으로 (최대 5개? 3개? 까지) 출력
            for(SyllabusDtoForStu s : syllabusDtoList){
                List<LectureVideo> lectureVideoList=lectureVideoService.findBySyllabus_IdSubmitEndAfterOrderBySubmitEndAsc(s.getId(),new Date());

                for(Iterator<LectureVideo> iter = lectureVideoList.iterator(); iter.hasNext();){                  // 수강한 강의라면 목록에서 삭제
                    LectureVideo lv=iter.next();
                    if(attendanceService.findBySyllabus_IdAndStudent_Id(s.getId(),username).getWeek(lv.getId()) != Attendance.NOINPUT)
                        iter.remove();
                }

                List<LectureVideoDto> lectureVideoDtoList=new ArrayList<>();                                // dto 로 변환해서 syllabusDto 내부에 set
                lectureVideoList.forEach(lv -> lectureVideoDtoList.add(new LectureVideoDto(lv)));
                s.setLectureVideoDtoList(lectureVideoDtoList);

                // 이 과목에서 수강 마감일 젤 빠른 강의가 여러 개일 경우 처리
                if(lectureVideoDtoList.isEmpty()==false)
                    s.setUrgentLectureVideos(lectureVideoDtoList);
            }


            model.addAttribute("MainPageDto", StudentMainPageDto.builder()                       // studentMainPageDto 를 view 에 전달
                                                                            .id(username)
                                                                            .name(student.getName())
                                                                            .noLectures(syllabusDtoList.isEmpty())
                                                                            .syllabusDtoList(syllabusDtoList)
                                                                            .cellDtoList(cellDtoList)
                                                                            .build());
            return "mainpage/student-mainpage";
        }
}
