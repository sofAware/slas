package kr.sofaware.slas.function.total.controller;

import kr.sofaware.slas.entity.*;
import kr.sofaware.slas.function.mainpage.dto.NoticeDto;
import kr.sofaware.slas.function.total.dto.LectureVideoDto;
import kr.sofaware.slas.service.*;
import kr.sofaware.slas.function.total.dto.AssignmentDto;
import kr.sofaware.slas.function.total.dto.QnADto;
import kr.sofaware.slas.function.total.dto.QuizDto;
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
@RequestMapping("/p")
public class ProfessorTotalPageController {
    private final SyllabusService syllabusService;
    private final NoticeService noticeService;
    private final QuizService quizService;
    private final AssignmentService assignmentService;
    private final QnaService qnaService;
    private final LectureVideoService lectureVideoService;
    private final LectureFileService lectureFileService;

    //교수 강의 종합 페이지
    @GetMapping("total")
    public String professorTotal(Model model, Principal principal,
                                 @Nullable @RequestParam("year-semester") String yearSemester,
                                 @Nullable @RequestParam("syllabus-id") String syllabusId) {

        //교수가 강의한 강의들 조회
        Map<String, List<Syllabus>> lectures = syllabusService.mapAllByProfessorId(principal.getName());

        //학기 선택이 없는 요청이면 제일 최근 학기 시간표
        if (syllabusId == null) {
            if (yearSemester == null) {
                ArrayList<String> yearSemesters = new ArrayList<>(lectures.keySet());
                // 이 사람이 강의한 수업이 없을 경우 그냥 리턴
                if (yearSemesters.isEmpty())
                    return "total/professor-totalpage";

                yearSemester = yearSemesters.get(0);
            }

            // 첫번째 과목
            syllabusId = lectures.get(yearSemester).get(0).getId();
        } else if (yearSemester == null) {
            yearSemester = syllabusId.substring(0, 4);
        }

        Map<String, String> formatYS = new TreeMap<>(Collections.reverseOrder());
        lectures.keySet().forEach(s -> formatYS.put(s, Syllabus.formatYearSemester(s)));

        //학기 선택 리스트
        model.addAttribute("mapYS", formatYS);
        model.addAttribute("yearSemester", Syllabus.formatYearSemester(yearSemester));
        model.addAttribute("formatYS", Syllabus.formatYearSemester(yearSemester));

        // 강의 선택 리스트
        model.addAttribute("syllabuses", lectures.get(yearSemester));
        String finalSyllabusId = syllabusId;
        model.addAttribute("selectedSyllabusName",
                lectures.get(yearSemester).stream().filter(s -> s.getId().equals(finalSyllabusId))
                        .findAny().get().getName()+" ("+lectures.get(yearSemester).stream().filter(s -> s.getId().equals(finalSyllabusId))
                        .findAny().get().formatClassTime()+")");


        // 공지사항
        List<NoticeDto> noticeDtoList=noticeService.findFirst3ByCategoryAndSyllabus_IdOrderByDateDesc(syllabusId);


        // 퀴즈 목록
        List<QuizDto> quizDtoList=new ArrayList<>();

        Map<String,List<Quiz>> quizMap=quizService.findBySyllabus_IdAndGroupByQuiz_Id(syllabusId);

        // quizMap 의 keySet 들에 대해서 QuizDto 를 생성해 quizDtoList 를 채워주면 됨
        for(String quizId : quizMap.keySet()){
            QuizDto quizDto=new QuizDto(quizMap.get(quizId).get(0));                        // list 의 가장 첫번째 Quiz 엔티티를 이용해서 채운다 ~!~
            quizDtoList.add(quizDto);
        }


        // 과제 목록
        List<Assignment> assignmentList = assignmentService.findBySyllabus_IdOrderBySubmitEndAsc(syllabusId);
        List<AssignmentDto> assignmentDtoList=new ArrayList<>();                                                    // dto 로 변환
        assignmentList.forEach(assignment -> assignmentDtoList.add(new AssignmentDto(assignment)));


        // QnA 게시판
        List<Board> qnAList= qnaService.getQnAListOrderByDateDesc(syllabusId);
        List<QnADto> qnADtoList=new ArrayList<>();
        qnAList.forEach(qnA -> qnADtoList.add(new QnADto(qnA)));


        // 강의 영상 목록
        List<LectureVideo> lectureVideoList = new ArrayList<>();
        lectureVideoList.addAll(lectureVideoService.listAll(syllabusId));

        List<LectureVideoDto> lectureVideoDtoList=new ArrayList<>();
        lectureVideoList.forEach(lv -> lectureVideoDtoList.add(new LectureVideoDto(lv)));


        // 강의 자료 목록
        List<Board> lectureFileList=new ArrayList<>();
        lectureFileList.addAll(lectureFileService.listAll(syllabusId));


        model.addAttribute("noticeList",noticeDtoList);                             // 공지 사항
        model.addAttribute("assignmentList",assignmentDtoList);                     // 과제 목록
        model.addAttribute("quizList",quizDtoList);                                 // 퀴즈 목록
        model.addAttribute("qnAList",qnADtoList);                                   // 질문 게시판 목록
        model.addAttribute("lectureVideoList",lectureVideoDtoList);                 // 강의 영상 목록
        model.addAttribute("lectureFileList",lectureFileList);                      // 강의 자료 목록


        return "total/professor-totalpage";
    }
}
