package kr.sofaware.slas.entity;

import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
public class LecturePK implements Serializable {
    private Student student;
    private Syllabus syllabus;
}
