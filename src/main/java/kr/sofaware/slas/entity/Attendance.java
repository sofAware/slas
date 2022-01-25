package kr.sofaware.slas.entity;

import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class Attendance implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;        // 학생

    @Id
    @ManyToOne
    @JoinColumn(name = "syllabus_id")
    private Syllabus syllabus;      // 강의

    private int week1, week2, week3, week4, week5, week6, week7, week8, week9, week10;  // 출석 1~10주차 (1: 출석, 2: 지각, 3: 결석)
    private int week11, week12, week13, week14, week15, week16;                         // 출석 11~16주차

    @Override
    public int hashCode() {
        return Objects.hash(student, syllabus);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || Hibernate.getClass(this) != Hibernate.getClass(obj))
            return false;

        Lecture o = (Lecture) obj;
        return Objects.equals(student, o.getStudent()) &&
                Objects.equals(syllabus, o.getSyllabus());
    }
}
