package kr.sofaware.slas.entity;

import lombok.Getter;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
public class Lecture implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;            // 학생

    @Id
    @ManyToOne
    @JoinColumn(name = "syllabus_id")
    private Syllabus syllabus;          // 강의

    @Id private int year;               // 년도
    @Id private int semester;           // 학기

    private int grade;                  // 학점

    @Override
    public int hashCode() {
        return Objects.hash(student, syllabus, year, semester);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || Hibernate.getClass(this) != Hibernate.getClass(obj)) return false;

        Lecture o = (Lecture) obj;
        return Objects.equals(student, o.getStudent()) &&
                Objects.equals(syllabus, o.getSyllabus()) &&
                Objects.equals(year, o.getYear()) &&
                Objects.equals(semester, o.getSemester());
    }
}
