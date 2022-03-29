package kr.sofaware.slas.function.quiz;


import kr.sofaware.slas.entity.*;
import kr.sofaware.slas.service.AttendanceService;
import kr.sofaware.slas.service.MemberService;
import kr.sofaware.slas.service.QuizService;
import kr.sofaware.slas.service.SyllabusService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/p/quiz")
public class quizProfessorController {
    private final MemberService memberService;
    private final AttendanceService attendanceService;
    private final SyllabusService syllabusService;
    private final QuizService quizService;


    @GetMapping("/list")
    public String getQuiz(Model model, Authentication authentication, Principal principal,
                                @Nullable @RequestParam("year-semester") String yearSemester,
                                @Nullable @RequestParam("syllabus-id") String syllabusId,
                                @Nullable @RequestParam("week-list") String week) {
        Collection<? extends GrantedAuthority> auth = authentication.getAuthorities();
        String Id = principal.getName();

        model.addAttribute("id", Id);
        model.addAttribute("auth", auth);

        Map<String, List<Syllabus>> lectures = syllabusService.mapAllByProfessorId(principal.getName());

        if (syllabusId == null) {
            if (yearSemester == null) {
                ArrayList<String> yearSemesters = new ArrayList<>(lectures.keySet());
                // 이 사람이 들었던 수업이 없을 경우 그냥 리턴
                if (yearSemesters.isEmpty())
                    return "notice/sNotice";

                yearSemester = yearSemesters.get(0);
            }
            syllabusId = lectures.get(yearSemester).get(0).getId();
        } else if (yearSemester == null) {
            yearSemester = syllabusId.substring(0, 4);
        }





        Map<String, String> formatYS = new TreeMap<>(Collections.reverseOrder());
        lectures.keySet().forEach(s -> formatYS.put(s, Syllabus.formatYearSemester(s)));

        // 학기 선택
        model.addAttribute("mapYS", formatYS);
        model.addAttribute("yearSemester", Syllabus.formatYearSemester(yearSemester));

        // 강의 선택
        model.addAttribute("syllabuses", lectures.get(yearSemester));
        String finalSyllabusId = syllabusId;
        model.addAttribute("selectedSyllabusName",
                lectures.get(yearSemester).stream().filter(s -> s.getId().equals(finalSyllabusId))
                        .findAny().get().getName());

        List<Quiz> quizzes = new ArrayList<>();
        if (syllabusId == null || syllabusId.isEmpty()) {
            lectures.get(yearSemester).forEach(syllabus ->
                    quizzes.addAll(quizService.listAll(syllabus.getId())));

            // 템플릿에서 강의명과 강의시간을 표시하기 위해 (isEmpty 판별) 추가
            model.addAttribute("selectedSyllabusId", "");
            model.addAttribute("selectedSyllabusName", "전체");
        }

        else {
            quizzes.addAll(quizService.listAll(syllabusId));

            // 선택된 강의 lectures에서 찾아서 강의명 입력
            String finalSyllabusId1 = syllabusId;
            Syllabus syllabus = lectures
                    .get(yearSemester)
                    .stream()
                    .filter(s -> s.getId().equals(finalSyllabusId1))
                    .findAny()
                    .get();
            model.addAttribute("selectedSyllabusId", syllabus.getId());

        }


        List<Quiz> quizzess = quizService.listAll(syllabusId);
        model.addAttribute("quizzess",quizzes);

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
        List<Attendance> attendances = attendanceService.listAll(Id,syllabusId);
        model.addAttribute("attendances", attendances);
        model.addAttribute("quizList",quizDtoList);


        return "quiz/pQuiz";
    }

    @GetMapping("/make")
    public String makeQuiz(Model model, Authentication authentication, Principal principal,
                                @Nullable @RequestParam("year-semester") String yearSemester,
                                @Nullable @RequestParam("syllabus-id") String syllabusId) {
        Collection<? extends GrantedAuthority> auth = authentication.getAuthorities();
        String Id = principal.getName();

        model.addAttribute("id", Id);
        model.addAttribute("auth", auth);

        Map<String, List<Syllabus>> lectures = syllabusService.mapAllByProfessorId(principal.getName());

        if (syllabusId == null) {
            if (yearSemester == null) {
                ArrayList<String> yearSemesters = new ArrayList<>(lectures.keySet());
                // 이 사람이 들었던 수업이 없을 경우 그냥 리턴
                if (yearSemesters.isEmpty())
                    return "notice/sNotice";

                yearSemester = yearSemesters.get(0);
            }
            syllabusId = lectures.get(yearSemester).get(0).getId();
        } else if (yearSemester == null) {
            yearSemester = syllabusId.substring(0, 4);
        }

        Map<String, String> formatYS = new TreeMap<>(Collections.reverseOrder());
        lectures.keySet().forEach(s -> formatYS.put(s, Syllabus.formatYearSemester(s)));

        // 학기 선택
        model.addAttribute("mapYS", formatYS);
        model.addAttribute("yearSemester", Syllabus.formatYearSemester(yearSemester));

        // 강의 선택
        model.addAttribute("syllabuses", lectures.get(yearSemester));
        String finalSyllabusId = syllabusId;
        model.addAttribute("selectedSyllabusName",
                lectures.get(yearSemester).stream().filter(s -> s.getId().equals(finalSyllabusId))
                        .findAny().get().getName());

        List<Quiz> quizzes = new ArrayList<>();
        if (syllabusId == null || syllabusId.isEmpty()) {
            lectures.get(yearSemester).forEach(syllabus ->
                    quizzes.addAll(quizService.listAll(syllabus.getId())));

            // 템플릿에서 강의명과 강의시간을 표시하기 위해 (isEmpty 판별) 추가
            model.addAttribute("selectedSyllabusId", "");
            model.addAttribute("selectedSyllabusName", "전체");
        }

        else {
            quizzes.addAll(quizService.listAll(syllabusId));

            // 선택된 강의 lectures에서 찾아서 강의명 입력
            String finalSyllabusId1 = syllabusId;
            Syllabus syllabus = lectures
                    .get(yearSemester)
                    .stream()
                    .filter(s -> s.getId().equals(finalSyllabusId1))
                    .findAny()
                    .get();
            model.addAttribute("selectedSyllabusId", syllabus.getId());

        }


        List<Quiz> quizzess = quizService.listAll(syllabusId);
        model.addAttribute("quizzess",quizzes);

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
        List<Attendance> attendances = attendanceService.listAll(Id,syllabusId);
        model.addAttribute("attendances", attendances);
        model.addAttribute("quizList",quizDtoList);



        //진짜
        // 학정번호가 넘어왔으면 그걸로 강의 아니면 교수한 강의 최근 1개
        Optional<Syllabus> syllabus = syllabusId == null ?
                syllabusService.findFirstByProfessor_IdOrderByIdDesc(principal.getName()) :
                syllabusService.findById(syllabusId);

        // 해당 강의가 없다면 잘못된 요청
        if (syllabus.isEmpty())
            return "error/400";



        // 작성
        model.addAttribute("syllabus", syllabus.get());
        return "quiz/pQuizNew";
    }

    @PostMapping("/make")
    public String makingQuiz(QuizSaveDto quizDto, Model model, Principal principal) throws IOException {


        //퀴즈
        Quiz.QuizBuilder builder = Quiz.builder()
                .syllabus(syllabusService.findById(quizDto.getSyllabusId()).get())
                .id(quizDto.getId())
                .name(quizDto.getName())
                .questionNum(quizDto.getQuestionNum())
                .category(quizDto.getCategory())
                .question(quizDto.getQuestion())
                .correctAnswer(quizDto.getCorrectAnswer())
                .submitEnd(quizDto.getSubmitEnd())
                .submitStart(quizDto.getSubmitStart())
                .score(quizDto.getScore());


        Quiz quiz = builder.build();
        quizService.save(quiz);

//        List<Quiz> quizTest=quizService.findAllBySyllabus_IdAndId("a","a");
//        model.addAttribute("quizTest",quizTest);

        return "redirect:/p/quiz/make/"+ quizDto.getId() + "&" + syllabusService.findById(quizDto.getSyllabusId()).get().getId();
    }

    @GetMapping("/make/{testNum}&{syNo}")
    public String editQuiz(Model model, Authentication authentication, Principal principal,
                           @PathVariable String testNum,
                           @PathVariable String syNo,
                           @Nullable @RequestParam("year-semester") String yearSemester,
                           @Nullable @RequestParam("syllabus-id") String syllabusId) {
        Collection<? extends GrantedAuthority> auth = authentication.getAuthorities();
        String Id = principal.getName();

        model.addAttribute("id", Id);
        model.addAttribute("auth", auth);

        //진짜
        List<Quiz> quizTest=quizService.findAllBySyllabus_IdAndId(syNo,testNum);
        model.addAttribute("quizTest",quizTest);
        model.addAttribute("syllabusId",syNo);

        Optional<Syllabus> syllabus = syllabusId == null ?
                syllabusService.findFirstByProfessor_IdOrderByIdDesc(principal.getName()) :
                syllabusService.findById(syllabusId);

        // 해당 강의가 없다면 잘못된 요청
        if (syllabus.isEmpty())
            return "error/400";



        // 작성
        model.addAttribute("syllabus", syllabus.get());

        return "quiz/pQuizmake";
    }

    @PostMapping("/make/{testNum}&{syNo}")
    public String editingQuiz(QuizSaveDto quizDto,@PathVariable String syNo,@PathVariable String testNum ,Model model, Principal principal) throws IOException {

        Quiz.QuizBuilder builder = Quiz.builder()
                .syllabus(syllabusService.findById(syNo).get())
                .id(quizDto.getId())
                .name(quizDto.getName())
                .questionNum(quizDto.getQuestionNum())
                .category(quizDto.getCategory())
                .question(quizDto.getQuestion())
                .correctAnswer(quizDto.getCorrectAnswer())
                .submitEnd(quizDto.getSubmitEnd())
                .submitStart(quizDto.getSubmitStart())
                .score(quizDto.getScore());


        Quiz quiz = builder.build();
        quizService.save(quiz);
        return "redirect:/p/quiz/make/"+ quizDto.getId() + "&" + syllabusService.findById(syNo).get().getId();
    }

}

