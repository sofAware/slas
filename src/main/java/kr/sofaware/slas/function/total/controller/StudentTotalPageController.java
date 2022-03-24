package kr.sofaware.slas.function.total.controller;

import kr.sofaware.slas.entity.*;
import kr.sofaware.slas.function.mainpage.dto.NoticeDto;
import kr.sofaware.slas.function.total.dto.*;
import kr.sofaware.slas.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/s")
public class StudentTotalPageController {
    private final LectureService lectureService;
    private final NoticeService noticeService;
    private final AssignmentService assignmentService;
    private final QuizService quizService;
    private final LectureFileService lectureFileService;
    private final AttendanceService attendanceService;
    private final LectureVideoService lectureVideoService;


    //학생 강의 종합 페이지
    @GetMapping("total")
    public String studentTotal(Model model, Principal principal,
                               @Nullable @RequestParam("year-semester") String yearSemester,
                               @Nullable @RequestParam("syllabus-id") String syllabusId) {

        // 사용자의 수강 학기와 과목들 조회
        Map<String, List<Syllabus>> lectures = lectureService.mapAllByStudentId(principal.getName());

        // 학기, 과목 선택 없는 요청이면 제일 최근 학기와 사전 순 빠른 학정번호를 선택
        if (syllabusId == null) {
            if (yearSemester == null) {
                ArrayList<String> yearSemesters = new ArrayList<>(lectures.keySet());
                // 이 사람이 들었던 수업이 없을 경우 그냥 리턴
                if (yearSemesters.isEmpty())
                    return "total/student-totalpage";

                yearSemester = yearSemesters.get(0);
            }

            // 첫번째 과목
            syllabusId = lectures.get(yearSemester).get(0).getId();
        } else if (yearSemester == null) {
            yearSemester = syllabusId.substring(0, 4);
        }

        Map<String, String> formatYS = new TreeMap<>(Collections.reverseOrder());
        lectures.keySet().forEach(s -> formatYS.put(s, Syllabus.formatYearSemester(s)));
        // 학기 선택 리스트
        model.addAttribute("mapYS", formatYS);
        model.addAttribute("yearSemester", Syllabus.formatYearSemester(yearSemester));

        // 강의 선택 리스트
        model.addAttribute("syllabuses", lectures.get(yearSemester));
        String finalSyllabusId = syllabusId;
        model.addAttribute("selectedSyllabusName",
                lectures.get(yearSemester).stream().filter(s -> s.getId().equals(finalSyllabusId))
                        .findAny().get().getName());


        // 공지사항
        List<NoticeDto> noticeDtoList=noticeService.findFirst3ByCategoryAndSyllabus_IdOrderByDateDesc(syllabusId);


        // 퀴즈 목록
        List<QuizDto> quizDtoList=new ArrayList<>();

        Map<String,List<Quiz>> quizMap=quizService.findBySyllabus_IdAndGroupByQuiz_Id(syllabusId);

        // quizMap 의 keySet 들에 대해서 QuizDto 를 생성해 quizDtoList 를 채워주면 됨
        for(String quizId : quizMap.keySet()){
            QuizDto quizDto=new QuizDto(quizMap.get(quizId).get(0));                        // list 의 가장 첫번째 Quiz 엔티티를 이용해서 채운다 ~!~

            // 퀴즈 응시 상태 update
            quizDto.setSubmitted(quizService.isQuizSubmitted(principal.getName(), syllabusId, quizDto.getId(), quizMap.get(quizId).size()));
            // 취득 점수 update
            quizDto.setTotalScore(quizService.getTotalScore(syllabusId,quizDto.getId()));
            quizDto.setAcquiredScore(quizService.getAcquiredScore(syllabusId,quizDto.getId()));

            quizDtoList.add(quizDto);
        }


        // 과제 목록
        List<Assignment> assignmentList = assignmentService.findBySyllabus_IdOrderBySubmitEndAsc(syllabusId);
        List<AssignmentDto> assignmentDtoList=new ArrayList<>();                                        // dto 로 변환
        assignmentList.forEach(assignment -> assignmentDtoList.add(new AssignmentDto(assignment)));
        // 과제 제출 상태 확인 => 제출 상태 dto 에 업뎃
        for(AssignmentDto a : assignmentDtoList)
            a.setSubmitTrue(assignmentService.existsByAssignment_IdAndMember_Id(a.getId(),principal.getName()));


        // 과목 현황 -> 완료한 과제 개수
        int finishedQuiz=0;                                        // quizDtoList 에서 submitted == true 인것들의 개수
        for(QuizDto q : quizDtoList)
            if(q.getSubmitted()==true)
                finishedQuiz++;

        // 강의 영상 목록
        List<LectureVideo> lectureVideoList = new ArrayList<>();
        lectureVideoList.addAll(lectureVideoService.listAll(syllabusId));

        List<LectureVideoDto> lectureVideoDtoList=new ArrayList<>();
        lectureVideoList.forEach(lv -> lectureVideoDtoList.add(new LectureVideoDto(lv)));

        // 과목 현황 -> 완료한 강의 개수 (출석 제대로한 강의 & 지각이지만 그래도 들은 강의만 인정)
        int finishedLectureVideo=0;
        for(LectureVideoDto lv : lectureVideoDtoList){
            if(attendanceService.findBySyllabus_IdAndStudent_Id(syllabusId, principal.getName()).getWeek(Integer.parseInt(lv.getId()))== Attendance.ATTEND||attendanceService.findBySyllabus_IdAndStudent_Id(syllabusId, principal.getName()).getWeek(Integer.parseInt(lv.getId()))== Attendance.LATE)
                finishedLectureVideo++;
        }

        model.addAttribute("noticeList",noticeDtoList);                             // 공지 사항
        model.addAttribute("assignmentList",assignmentDtoList);                     // 과제 목록
        model.addAttribute("quizList",quizDtoList);                                 // 퀴즈 목록
        model.addAttribute("subjectStatus",SubjectStatusDto.builder()               // 과목 현황
                                                                        .finishedLecture(finishedLectureVideo)
                                                                        .totalLecture(lectureVideoList.size())
                                                                        .finishedAssignment(assignmentService.countSubmittedAssignment(syllabusId, principal.getName()))
                                                                        .totalAssignment(assignmentService.countBySyllabus_Id(syllabusId))
                                                                        .finishedQuiz(finishedQuiz)
                                                                        .totalQuiz(quizDtoList.size())
                                                                        .lectureFiles(lectureFileService.countLectureFiles(syllabusId))
                                                                        .build());
        model.addAttribute("attendance",new AttendanceDto(attendanceService.findBySyllabus_IdAndStudent_Id(syllabusId, principal.getName())));       // 출석 내역
        model.addAttribute("lectureVideoList",lectureVideoDtoList);                 // 강의 영상 목록

        return "total/student-totalpage";
    }
}
