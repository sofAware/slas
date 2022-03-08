package kr.sofaware.slas.entity;

import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
public class GradePK implements Serializable {
    private Member student;            // 학생
    private int year;                   // 년도
    private int semester;
}