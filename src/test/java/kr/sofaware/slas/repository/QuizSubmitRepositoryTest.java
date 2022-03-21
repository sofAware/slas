package kr.sofaware.slas.repository;

import kr.sofaware.slas.entity.Member;
import kr.sofaware.slas.entity.Quiz;
import kr.sofaware.slas.entity.QuizPK;
import kr.sofaware.slas.entity.QuizSubmit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QuizSubmitRepositoryTest {

    @Autowired QuizRepository quizRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired QuizSubmitRepository quizSubmitRepository;

    @Test
    public void save() {
        Quiz quiz = quizRepository.findById(new QuizPK("21-2-0101-3-0001-01", "1-1", 1)).get();
        Member student = memberRepository.findById("2019010101").get();

        QuizSubmit submit = new QuizSubmit(quiz, student, "경호의테스트답안", false, -2);

        quizSubmitRepository.save(submit);
    }
}