package kr.sofaware.slas.service;

import kr.sofaware.slas.entity.Attendance;
import kr.sofaware.slas.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
