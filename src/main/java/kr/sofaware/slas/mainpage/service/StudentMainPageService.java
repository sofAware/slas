package kr.sofaware.slas.mainpage.service;

import kr.sofaware.slas.entity.Assignment;
import kr.sofaware.slas.entity.Board;
import kr.sofaware.slas.entity.Lecture;
import kr.sofaware.slas.entity.Member;
//import kr.sofaware.slas.entity.Student;
import kr.sofaware.slas.mainpage.dto.AssignmentDto;
import kr.sofaware.slas.mainpage.dto.NoticeDto;
import kr.sofaware.slas.mainpage.dto.StudentDto;
import kr.sofaware.slas.mainpage.dto.SyllabusDto;
import kr.sofaware.slas.repository.AssignmentRepository;
import kr.sofaware.slas.repository.BoardRepository;
import kr.sofaware.slas.repository.LectureRepository;
import kr.sofaware.slas.repository.MemberRepository;
//import kr.sofaware.slas.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class StudentMainPageService {
    private final MemberRepository memberRepository;
    private final LectureRepository lectureRepository;
    private final BoardRepository boardRepository;
    private final AssignmentRepository assignmentRepository;

//    public StudentDto findById(String id){
//        Member entity=memberRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException(id));
//
//        return new StudentDto(entity);
//    }

//    public List<SyllabusDto> findByIdAndYearSemester(String id, int year, int semester){
//        String yearSemester= (Integer.toString(year)).substring(2)+"-"+Integer.toString(semester);      //년도 학기 걍 int 로 받고 "21-2" 이런식으로 이어붙여서 DB 찾음~~
//
//        List<SyllabusDto> syllabusDtoList=new ArrayList<>();
//
//        List<Lecture> lectureList=lectureRepository.findByIdAndYearSemester(id,yearSemester);
//
//        lectureList.forEach(lecture -> syllabusDtoList.add(new SyllabusDto(lecture.getSyllabus())));
//
//        return syllabusDtoList;
//    }

//    // board 테이블에서 ( category 는 1이고 && syllabus_id 는 syllabusDtoList.get(i).id ) 인 것들을 찾아서 등록일 빠른 순으로(datetime 내림차순) 정렬 => 위에서부터 레코드 3개만 가져오기 => noticeDtoList 에 noticeDto 로 add
//    public List<NoticeDto> findFirst3ByCategoryAndSyllabus_IdOrderByDateDesc(int category, String syllabus_id){
//        List<NoticeDto> noticeDtoList=new ArrayList<>();
//
//        List<Board> boardList=boardRepository.findFirst3ByCategoryAndSyllabus_IdOrderByDateDesc(category,syllabus_id);
//
//        boardList.forEach(board -> noticeDtoList.add(new NoticeDto(board)));
//
//        return noticeDtoList;
//    }

//    // assignment 테이블에서 syllabus_id == :syllabus_id 이고 submitEnd >= :currentDate 인 레코드들을 찾아 제출 마감기한 순으로 오름차순 정렬 => (과제 제출 여부와는 상관없이) 아직 제출기한이 남은 과제들을 제출기한 빠른 순으로 정렬해 가져옴
//    public List<AssignmentDto> findBySyllabus_IdSubmitEndAfterOrderBySubmitEndAsc(String syllabus_id, Date currentDate){
//        List<AssignmentDto> assignmentDtoList=new ArrayList<>();
//
//        List<Assignment> assignmentList=assignmentRepository.findBySyllabus_IdSubmitEndAfterOrderBySubmitEndAsc(syllabus_id,currentDate);
//
//        assignmentList.forEach(assignment -> assignmentDtoList.add(new AssignmentDto(assignment)));
//
//        return assignmentDtoList;
//    }

//    // board 테이블에서 과제 id = :assignment_id, 작성자 id = :member_id 인 레코드가 존재하는지 확인
//    public Boolean existsByAssignment_IdAndMember_Id(int assignment_id, String member_id){
//        return boardRepository.existsByAssignment_IdAndMember_Id(assignment_id,member_id);
//    }

}
