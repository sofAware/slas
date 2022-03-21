package kr.sofaware.slas.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizSubmitPK implements Serializable {
    private QuizPK quiz;
    private String student;
}
