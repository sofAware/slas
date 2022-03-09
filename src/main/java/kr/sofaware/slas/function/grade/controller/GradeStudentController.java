package kr.sofaware.slas.function.grade.controller;

import kr.sofaware.slas.entity.Member;
import kr.sofaware.slas.function.grade.dto.gradeDTO;
import kr.sofaware.slas.service.GradeService;
import kr.sofaware.slas.service.LectureService;
import kr.sofaware.slas.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
@Controller
@RequestMapping("/s")
public class GradeStudentController {

    private final MemberService memberService; //인적 사항 조회시 사용
    private final GradeService gradeService; //석차 조회
    private final LectureService lectureService; //학점 가져올 때 사용
    //private final SyllabusService syllabusService; //강의 정보 가져올 때 사용

    private gradeDTO tempGradeDTO;
    private List<gradeDTO> gradeDTOS;


    @GetMapping("grade")
    public String getStudentGrade(Authentication authentication, Principal principal, Model model) {

        String studentId = principal.getName();

        AtomicInteger geSubjectCredit = new AtomicInteger(); //교양 취득 학점
        AtomicInteger majorSubjectCredit = new AtomicInteger(); //전공 취득 학점

        AtomicInteger geSubjectCount = new AtomicInteger(); //교양 카운트
        AtomicInteger majorSubjectCount = new AtomicInteger(); //전공 카운트

        AtomicReference<Double> geSubjectGrade = new AtomicReference<>((double) 0); //교양 성적 학점 총합
        AtomicReference<Double> majorSubjectGrade = new AtomicReference<>((double) 0); //교양 성적 학점 총합

        //1. 인적 사항 조회
        Member studentInfo = memberService.loadUserByUsername(studentId);
        model.addAttribute("studentInfo", studentInfo);

        //2. 교양, 전공 취득학점, 평균 학점
        lectureService.findAllByStudentId(studentId).forEach(lecture -> {
            switch (lecture.getSyllabus().getCategory()) {
                case "전선":
                case "전필":
                    majorSubjectCredit.addAndGet(lecture.getSyllabus().getCredit());
                    majorSubjectGrade.updateAndGet(v -> (double) (v + lecture.getGrade()));
                    majorSubjectCount.addAndGet(1);
                    break;
                case "교필":
                case "교선":
                    geSubjectCredit.addAndGet(lecture.getSyllabus().getCredit());
                    geSubjectGrade.updateAndGet(v -> (double) (v + lecture.getGrade()));
                    geSubjectCount.addAndGet(1);
                    break;
            }
        });

        //취득 학점
        model.addAttribute("geCredit", geSubjectCredit);
        model.addAttribute("majorCredit", majorSubjectCredit);

        //평균 학점
        model.addAttribute("majorGradeAvg", Math.round((majorSubjectGrade.get() / majorSubjectCount.get()) * 100) / 100.0);
        model.addAttribute("allGradeAvg", Math.round(((majorSubjectGrade.get() + geSubjectGrade.get()) / (majorSubjectCount.get() + geSubjectCount.get())) * 100) / 100.0);

        //3. 석차 조회
        model.addAttribute("rankList", gradeService.findAllByStudentIdSorting(studentId));

        //4. 년도별 학점 리스트
        Map<String, List<gradeDTO>> gradeByYear = new HashMap<>();

        lectureService.mapLectureByStudentId(studentId).forEach((s, lectures) -> {
            
            gradeDTOS = new ArrayList<>(); //년도별 List 할당
            
            lectures.forEach(lecture -> {
                tempGradeDTO = new gradeDTO(
                        lecture.getSyllabus().getId(),
                        lecture.getSyllabus().getName(),
                        lecture.getSyllabus().getProfessor().getMajor(),
                        lecture.getSyllabus().getCategory(),
                        lecture.getSyllabus().getCredit(),
                        lecture.getGrade()
                );
                gradeDTOS.add(tempGradeDTO);
            });
            gradeByYear.put(s, gradeDTOS);
            //model.addAttribute("t", gradeDTOS);
        });

        //model.addAttribute("gradeKeySet", lectureService.mapLectureByStudentId(studentId).keySet());
        model.addAttribute("gradeByYear", gradeByYear);

        return "grade/sGradeMain";
    }
}
