package kr.sofaware.slas.service;

import kr.sofaware.slas.entity.Grade;
import kr.sofaware.slas.entity.Syllabus;
import kr.sofaware.slas.repository.GradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
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

    public Optional<Grade> findByStudentIdAndYearAndSemester(String studentId, int year, int semester)
    {
        return gradeRepository.findByStudent_IdAndYearAndSemester(studentId, year, semester);
    }

    public List<Optional<Grade>> findAllByYearAndSemesterOrderByGradeAvgDESC(int year, int semester)
    {
        return gradeRepository.findAllByYearAndSemesterOrderByGradeAvgDesc(year, semester);
    }

    public Grade save(Grade grade)
    {
        return gradeRepository.save(grade);
    }
}
