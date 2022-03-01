package kr.sofaware.slas.service;

import kr.sofaware.slas.entity.Grade;
import kr.sofaware.slas.entity.Syllabus;
import kr.sofaware.slas.repository.GradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GradeService {
    private final GradeRepository gradeRepository;

    //https://chickenpaella.tistory.com/24 다중 조건 정렬
    //https://stackoverflow.com/questions/12542185/sort-a-java-collection-object-based-on-one-field-in-it
    //년도 오름차순 + 학기 오름차순
    public List<Grade> findAllByStudentIdSorting(String studentId)
    {
       return gradeRepository.findAllByStudent_Id(studentId).stream().sorted(Comparator
                       .comparing(Grade::getYear).reversed()
                       .thenComparing(Grade::getSemester).reversed())
               .collect(Collectors.toList());
    }
}
