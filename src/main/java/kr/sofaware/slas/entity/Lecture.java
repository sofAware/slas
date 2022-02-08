package kr.sofaware.slas.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@IdClass(LecturePK.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Lecture implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Member student;            // 학생

    @Id
    @ManyToOne
    @JoinColumn(name = "syllabus_id")
    private Syllabus syllabus;          // 강의

    private double grade;                  // 학점
}

@EqualsAndHashCode
class LecturePK implements Serializable{
    private Member student;            // 학생
    private Syllabus syllabus;          // 강의
}