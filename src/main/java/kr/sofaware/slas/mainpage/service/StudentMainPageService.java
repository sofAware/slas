package kr.sofaware.slas.mainpage.service;

import kr.sofaware.slas.entity.Lecture;
import kr.sofaware.slas.entity.Member;
//import kr.sofaware.slas.entity.Student;
import kr.sofaware.slas.mainpage.dto.StudentDto;
import kr.sofaware.slas.mainpage.dto.SyllabusDto;
import kr.sofaware.slas.repository.LectureRepository;
import kr.sofaware.slas.repository.MemberRepository;
//import kr.sofaware.slas.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class StudentMainPageService {
    private final MemberRepository memberRepository;
    private final LectureRepository lectureRepository;

    public StudentDto findById(String id){
        Member entity=memberRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException(id));

        return new StudentDto(entity);
    }

    public List<SyllabusDto> findByIdAndYearSemester(String id, int year, int semester){
        String yearSemester= (Integer.toString(year)).substring(2)+"-"+Integer.toString(semester);      //년도 학기 걍 int 로 받고 "21-2" 이런식으로 이어붙여서 DB 찾음~~

        List<SyllabusDto> syllabusDtoList=new ArrayList<>();

        List<Lecture> lectureList=lectureRepository.findByIdAndYearSemester(id,yearSemester);

        lectureList.forEach(lecture -> syllabusDtoList.add(new SyllabusDto(lecture.getSyllabus())));

        return syllabusDtoList;
    }
}
