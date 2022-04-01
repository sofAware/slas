package kr.sofaware.slas.service;

import kr.sofaware.slas.entity.Assignment;
import kr.sofaware.slas.entity.Quiz;
import kr.sofaware.slas.entity.QuizPK;
import kr.sofaware.slas.entity.QuizSubmit;
import kr.sofaware.slas.repository.QuizRepository;
import kr.sofaware.slas.repository.QuizSubmitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.lang.Integer.parseInt;

@RequiredArgsConstructor
@Service
public class QuizService {
    private final QuizRepository quizRepository;
    private final QuizSubmitRepository quizSubmitRepository;

    /**
     * 해당 과목의 퀴즈들을 quiz_id 별로 묶어서 반환. quizRepository 에서 questionNum 으로 정렬해서 넘겨주기 때문에 문제번호 정렬 됨 + TreeMap 이라서 quiz_id 도 정렬 됨
     * @author 정지민
     * @param syllabus_id
     * @return Map<key: quiz_id, value: 해당 quiz_id 에 해당하는 문항들(Quiz 엔티티)의 리스트>
     */
    public Map<String, List<Quiz>> findBySyllabus_IdAndGroupByQuiz_Id(String syllabus_id){
        Map<String, List<Quiz>> map=new TreeMap<String, List<Quiz>>();

        quizRepository.findBySyllabus_IdOrderByQuestionNumAsc(syllabus_id).forEach(quiz -> {
            if(!map.containsKey(quiz.getId()))
                map.put(quiz.getId(), new ArrayList<>());

            map.get(quiz.getId()).add(quiz);
        });

        return map;
    }

    /**
     * quiz_submit 테이블에서 해당 과목의 해당 퀴즈에 대해 학생이 제출한 답안 레코드가 몇개인지 count -> 해당 퀴즈의 총 문항 수와 비교해서 학생의 퀴즈 응시 여부 판단
     * @author 정지민
     * @param studentId, syllabusId, quizId, 해당 quizId 퀴즈의 총 문항 수
     * @return 응시 여부
     */
    public boolean isQuizSubmitted(String studentId, String syllabusId, String quizId, int questionCount){
        if(questionCount==quizSubmitRepository.countByStudent_IdAndQuiz_Syllabus_IdAndQuiz_Id(studentId,syllabusId,quizId))
            return true;
        return false;
    }

    /**
     * 해당 퀴즈의 총점, 즉 만점이 몇점인지를 구함
     * @author 정지민
     * @param syllabusId, quizId
     * @return 퀴즈 총점
     */
    public int getTotalScore(String syllabusId, String quizId){
        int totalScore=0;
        List<Quiz> quizList=quizRepository.findBySyllabus_IdAndId(syllabusId,quizId);

        for(Quiz q : quizList)
            totalScore+=q.getScore();

        return totalScore;
    }

    /**
     * 해당 퀴즈에 대해 취득한 점수를 구함
     * @author 정지민
     * @param syllabusId, quizId, studentId
     * @return 취득 총점
     */
    public int getAcquiredScore(String syllabusId, String quizId, String studentId){
        int acquiredScore=0;
        List<QuizSubmit> quizSubmitList=quizSubmitRepository.findByQuiz_Syllabus_IdAndQuiz_IdAndStudent_Id(syllabusId,quizId,studentId);

        for(QuizSubmit q : quizSubmitList)
            acquiredScore+=q.getScore();

        return acquiredScore;
    }

    /**
     * @author 박소현
     */
    public List<Quiz> listAll() {
        return quizRepository.findAll();
    }
    /**
     * @author 박소현
     */
    public List<Quiz> listAll(String SyllabusId) {
        return quizRepository.findAllBySyllabus_Id(SyllabusId);
    }
    /**
     * @author 박소현
     */
    public List<Quiz> listAll(String Id,String SyllabusId) {
        return quizRepository.findBySyllabus_IdAndId(SyllabusId,Id);
    }

    /**
     * @author 박소현
     */
    public List<QuizSubmit> alistAll() {
        return quizSubmitRepository.findAll();
    }
    /**
     * @author 박소현
     */
    public List<QuizSubmit> alistAll(String SyllabusId) {
        return quizSubmitRepository.findAllByQuiz_Syllabus_Id(SyllabusId);
    }

    /**
     * @author 박소현
     */
    public void save(QuizSubmit quizSubmit) {
        quizSubmitRepository.save(quizSubmit);
    }
    /**
     * @author 박소현
     */
    public void save(Quiz quiz) {
        quizRepository.save(quiz);
    }
    /**
     * @author 박소현
     */
    public Optional<QuizSubmit> read(String SyllabusId){
        return quizSubmitRepository.findByQuiz_SyllabusId(SyllabusId);
    }

    public  List<Quiz> findAllBySyllabus_IdAndId(String SyllabusId,String Id){
        return quizRepository.findAllBySyllabus_IdAndId(SyllabusId,Id);
    }

    public Quiz findById(String syNo, String testNum, String testId){
        return   quizRepository.findById(new QuizPK(syNo, testNum, parseInt(testId))).get();
    }


//    public Optional<Quiz> read(String SyllabusId,String Id) {
//        return quizRepository.findById(Quiz);
//    }
}
