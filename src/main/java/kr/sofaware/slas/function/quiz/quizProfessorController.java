package kr.sofaware.slas.function.quiz;


import kr.sofaware.slas.entity.*;
import kr.sofaware.slas.function.board.AssignmentSubmitInfo;
import kr.sofaware.slas.function.mail.MyMailSender;
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
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.Integer.parseInt;

@RequiredArgsConstructor
@Controller
@RequestMapping("/p/quiz")
public class quizProfessorController {
    private final MemberService memberService;
    private final AttendanceService attendanceService;
    private final SyllabusService syllabusService;
    private final QuizService quizService;
    private final MyMailSender myMailSender;
    private final LectureService lectureService;



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
            quizDto.setAcquiredScore(quizService.getAcquiredScore(syllabusId,quizDto.getId(), principal.getName()));

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
            quizDto.setAcquiredScore(quizService.getAcquiredScore(syllabusId,quizDto.getId(),principal.getName()));

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

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일"); //원하는 데이터 포맷 지정 String strNowDate = simpleDateFormat.format(nowDate);

        List<Lecture> lectures = lectureService.findAllBySyllabusId(quiz.getSyllabus().getId());
        lectures.forEach(l -> myMailSender.send(
                l.getStudent().getEmail(),
                "[퀴즈] " + quiz.getId() +":" + quiz.getName(),
                "http://slas.kr/s/quiz/list" ,
                quiz.getId()+ ": 새 퀴즈가 업로드 되었습니다\n 응시 가능 시작 일자: "+simpleDateFormat.format(quiz.getSubmitStart())+"\n응시 가능 마감 일자: "+simpleDateFormat.format(quiz.getSubmitEnd()))) ;



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

    @GetMapping("/make/delete/{testNum}&{syNo}&{testInNum}")
    public String delete(Principal principal,
                         @PathVariable String syNo,
                         @PathVariable String testNum,@PathVariable String testInNum) {

        String A = "redirect:/p/quiz/list";
        QuizPK optionalQuiz = new QuizPK(syNo, testNum, parseInt(testInNum));
        quizService.delete(optionalQuiz);
        if(testInNum.equals("1")){
            A = "redirect:/p/quiz/list";
        }else{
            A = "redirect:/p/quiz/make/"+testNum+"&"+syNo;
        }


        // 목록으로 리디렉션
        return A;
    }

    @GetMapping("/make/browse/{testNum}&{syNo}")
    public String browse(Principal principal,
                         @PathVariable String syNo,
                         @PathVariable String testNum,Model model) {

        List<Integer> studentScoreList=new ArrayList<>();

        List<QuizSubmit> quizSubmitList=quizService.findByQuiz_Syllabus_IdAndQuiz_Id(syNo,testNum);

        for(int i=0;i<quizSubmitList.size();i++){
            String studentId=quizSubmitList.get(i).getStudent().getId();
            studentScoreList.add(quizService.getAcquiredScore(syNo,testNum,studentId));
        }

        model.addAttribute("quizSubmitList",quizSubmitList); //학생 학번과 이름을 가져오기 위하
        model.addAttribute("studentScoreList",studentScoreList); //점수를 담아놓은 리스트 (학생의 리스트 순서대로)
        model.addAttribute("testNum",testNum);
        model.addAttribute("syNo",syNo);

        //퀴즈 열람 페이지
        return "quiz/pQuizResult";
    }


}


