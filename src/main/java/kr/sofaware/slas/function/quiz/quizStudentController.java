package kr.sofaware.slas.function.quiz;

import kr.sofaware.slas.entity.*;
import kr.sofaware.slas.repository.MemberRepository;
import kr.sofaware.slas.repository.QuizRepository;
import kr.sofaware.slas.repository.QuizSubmitRepository;
import kr.sofaware.slas.service.*;
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

import static java.lang.Integer.parseInt;

@RequiredArgsConstructor
@Controller
@RequestMapping("/s/quiz")
public class quizStudentController {
    private final LectureService lectureService; //강의 정보 받아올 곳 (강의 이름, 학정번호)
    private final AttendanceService attendanceService; //출석 체크 결과 받아올 곳
    private  final  MemberService memberService;
    private final QuizService quizService;
    private final QuizRepository quizRepository;
    private final MemberRepository memberRepository;
    private final QuizSubmitRepository quizSubmitRepository;

    @GetMapping("/list")
    public String getQuiz(Model model, Authentication authentication, Principal principal,
                                @Nullable @RequestParam("year-semester") String yearSemester,
                                @Nullable @RequestParam("syllabus-id") String syllabusId){

        Collection<? extends GrantedAuthority> auth = authentication.getAuthorities();
        String Id = principal.getName();

        model.addAttribute("id", Id);
        model.addAttribute("auth", auth);

        Map<String, List<Syllabus>> lectures = lectureService.mapAllByStudentId(principal.getName());

        // 학기, 과목 선택 없는 요청이면 제일 최근 학기와 사전 순 빠른 학정번호를 선택
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

        // 학기 선택 리스트
        model.addAttribute("mapYS", formatYS);
        model.addAttribute("yearSemester", Syllabus.formatYearSemester(yearSemester));

        // 강의 선택 리스트
        model.addAttribute("syllabuses", lectures.get(yearSemester));
        String finalSyllabusId = syllabusId;
        model.addAttribute("selectedSyllabusName",
                lectures.get(yearSemester).stream().filter(s -> s.getId().equals(finalSyllabusId))
                        .findAny().get().getName());

        // 강의 선택 없으면 해당 학기 전체 강의에 대한 공지사항 긁어오기
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
            quizDto.setAcquiredScore(quizService.getAcquiredScore(syllabusId,quizDto.getId(),principal.getName()));

            quizDtoList.add(quizDto);
        }
        List<Attendance> attendances = attendanceService.listAll(Id,syllabusId);
        model.addAttribute("attendances", attendances);
        model.addAttribute("quizList",quizDtoList);


        return "quiz/sQuiz";
    }

    @GetMapping("/detail/{testNum}&{syNo}")
    public String getQuizTest(Model model, Authentication authentication, Principal principal,
                              @PathVariable String testNum,
                                @PathVariable String syNo,
                          @Nullable @RequestParam("question_num") Integer question_num,
                              @PathVariable  @Nullable @RequestParam("syllabus-id") String syllabusId){


        Collection<? extends GrantedAuthority> auth = authentication.getAuthorities();
        String Id = principal.getName();
        model.addAttribute("id", Id);
        model.addAttribute("auth", auth);

        //퀴즈 번호랑 강의번호로 불러옴
        if(question_num==null){
            question_num=0;
        }else{
            question_num-=1;
        }
        List<QuizSubmit> quizzes=quizService.findByQuiz_Syllabus_IdAndQuiz_Id(syNo,testNum);
        List<Quiz> quizTest=quizService.findAllBySyllabus_IdAndId(syNo,testNum);
        model.addAttribute("quizzes",quizzes);
        model.addAttribute("quizTest",quizTest);
        model.addAttribute("syllabusId",syNo);
        model.addAttribute("question_num",question_num);


        return "quiz/sQuizTest";
    }

    @PostMapping("/detail/{testNum}&{syNo}")
//    public String postQuizTest(QuizSubmitDto quizSubmitDto, Model model,@PathVariable String testNum,
//                               @PathVariable String syNo,@RequestParam("testId") String testId){
    public String postQuizTest(Model model,QuizSubmitDto quizSubmitDto,
                               @PathVariable String syNo,Principal principal,
                               @PathVariable String testNum,@RequestParam("question_num") String question_num){

        List<Quiz> quizTest=quizService.findAllBySyllabus_IdAndId(syNo,testNum);

        Quiz quiz = quizService.findById(syNo, testNum, question_num);
        Member student = memberRepository.findById("2019010101").get();

        int scores=0;
        if(quiz.getCorrectAnswer().equals(quizSubmitDto.getAnswer())){
            scores=quiz.getScore();
        }

        //스코어는..? 답 값 가져와서 원래 답이랑 비교하고 맞으면 거기 점수 받아와서
        // 넣어주고 다르면 0점 처리
        //Member student = memberRepository.findById(Id).get();
        QuizSubmit.QuizSubmitBuilder builder=QuizSubmit.builder()
                .answer(quizSubmitDto.getAnswer())
                .student(memberService.loadUserByUsername(principal.getName()))
                .score(scores)
                .quiz(quiz);

        QuizSubmit quizSubmit1 =builder.build();
        //QuizSubmit submit = new QuizSubmit(quiz, student, "테스트제발되라", false, -2);

        quizService.save(quizSubmit1);

//        Quiz quiz = quizRepository.findById(new QuizPK("21-2-0101-3-0001-01", "1-1", 1)).get();
//        Member student = memberRepository.findById("2019010101").get();
//
//        QuizSubmit submit = new QuizSubmit(quiz, student, "테스트답안", false, -2);
//
//        quizSubmitRepository.save(submit);


/*        Optional<QuizSubmit> quizSubmit1=quizSubmitRepository.findByQuiz_SyllabusId(syllabusId);
        quizSubmit1.ifPresent(select -> {
            select.setAnswer(weekValue);
            quizService.save(select);
        });*/



        model.addAttribute("syllabusId",syNo);

//        QuizSubmit.QuizSubmitBuilder builder=QuizSubmit.builder()
//                .quiz(quizTest)

        return "redirect:/s/quiz/detail/" + testNum + "&" + syNo + "?question_num=" + question_num;
    }



}
