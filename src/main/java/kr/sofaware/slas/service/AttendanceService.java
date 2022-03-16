package kr.sofaware.slas.service;

import kr.sofaware.slas.entity.Assignment;
import kr.sofaware.slas.entity.Attendance;
import kr.sofaware.slas.entity.Syllabus;
import kr.sofaware.slas.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    /**
     * 학생의 해당 과목에 대한 출석 내역을 불러옴
     * @author 정지민
     * @param syllabus_id, student_id
     * @return 출석 내역 레코드
     */
    public Attendance findBySyllabus_IdAndStudent_Id(String syllabus_id, String student_id){
        return attendanceRepository.findBySyllabus_IdAndStudent_Id(syllabus_id, student_id);
    }

    /**
     * @author 박소현
     * @param attendance
     */
    public void create(Attendance attendance) {
        attendanceRepository.save(attendance);
    }

    /**
     * @author 박소현
     */
    public List<Attendance> listAll() {
        return attendanceRepository.findAll();
    }

    /**
     * @author 박소현
     */
    public List<Attendance> listAll(String Id, String SyllabusId) {
        return attendanceRepository.findAllByStudent_IdAndSyllabus_Id(Id ,SyllabusId);
    }

    /**
     * @author 박소현
     */
    public List<Attendance> listAll(String SyllabusId) {
        return attendanceRepository.findAllBySyllabus_Id(SyllabusId);
    }

    public List<Attendance> read(String StudentId, String SyllabusId) {
        return attendanceRepository.findAllByStudent_IdAndSyllabus_Id(StudentId,SyllabusId);
    }

    public Attendance save(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }

    //public Optional update(String SyllabusId,int week) {return attendanceRepository.findAllBySyllabusAndWeeksByList(SyllabusId,week);}

}
