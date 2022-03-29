package kr.sofaware.slas.function.quiz;

import lombok.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
public class QuizSaveDto {
    private String id;          //                        // 퀴즈 id
    private String name;           //                     // 퀴즈명
    private int questionNum;//
    private String submitStart;     //                      // 응시 시작일
    private String submitEnd;      //                       // 응시 마감일
    private String syllabusId;
    private String question;            // 문제
    private int category;               // 문제 유형 (1: 객관식, 2: 단답식, 3: 주관식)
    private String correctAnswer;       // 정답
    private int score; //배점

    public Date getSubmitStart() {
        try {
            return new SimpleDateFormat("yyyy-MM-dd")
                    .parse(submitStart);
        } catch (ParseException e) { }
        return new Date(0);
    }

    public Date getSubmitEnd() {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .parse(submitEnd + " 23:59:59");
        } catch (ParseException e) { }
        return new Date(0);
    }
}
