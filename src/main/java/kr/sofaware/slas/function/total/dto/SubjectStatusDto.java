package kr.sofaware.slas.function.total.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SubjectStatusDto {
    private int finishedLecture=0;                // 수강한 강의 개수
    private int totalLecture=0;                   // 전체 강의 개수

    private int finishedAssignment=0;             // 수행한 과제 개수
    private int totalAssignment=0;                // 전체 과제 개수

    private int finishedQuiz=0;                   // 응시한 퀴즈 개수
    private int totalQuiz=0;                      // 전체 퀴즈 개수

    private int lectureFiles=0;                   // 강의 자료 개수

    @Builder
    public SubjectStatusDto(int finishedLecture, int totalLecture, int finishedAssignment, int totalAssignment, int finishedQuiz, int totalQuiz, int lectureFiles){
        this.finishedLecture=finishedLecture;
        this.totalLecture=totalLecture;
        this.finishedAssignment=finishedAssignment;
        this.totalAssignment=totalAssignment;
        this.finishedQuiz=finishedQuiz;
        this.totalQuiz=totalQuiz;
        this.lectureFiles=lectureFiles;
    }
}
