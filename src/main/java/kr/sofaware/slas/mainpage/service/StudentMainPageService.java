package kr.sofaware.slas.mainpage.service;

import kr.sofaware.slas.mainpage.dto.StudentDto;
import kr.sofaware.slas.entity.Lecture;
import kr.sofaware.slas.entity.Student;
import kr.sofaware.slas.repository.LectureRepository;
import kr.sofaware.slas.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StudentMainPageService {
    private final StudentRepository studentRepository;
    private final LectureRepository lectureRepository;

    public StudentDto findById(String id){
        Student entity=studentRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException(id));

        return new StudentDto(entity);
    }

    public List<Lecture> findByIdAndYearSemester(String id, String yearSemester){

        //List<Lecture> lectureList=lectureRepository.findByStudent_Id(id); => 이거는 성공했음~~~~

        List<Lecture> lectureList=lectureRepository.findByIdAndYearSemester(id,yearSemester);

        return lectureList;
    }


}
