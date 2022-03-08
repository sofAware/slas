package kr.sofaware.slas.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Getter
@Builder
@ToString
@IdClass(LecturePK.class)
@NoArgsConstructor
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

