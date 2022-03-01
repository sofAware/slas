package kr.sofaware.slas.service;

import kr.sofaware.slas.entity.Attendance;
import kr.sofaware.slas.entity.Board;
import kr.sofaware.slas.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    public void create(Attendance attendance) {
        attendanceRepository.save(attendance);
    }
    public List<Attendance> listAll() {
        return attendanceRepository.findAll();
    }

    public List<Attendance> listAll(String Id, String SyllabusId) {
        return attendanceRepository.findAllByStudent_IdAndSyllabus_Id(Id ,SyllabusId);
    }

    public List<Attendance> listAll(String SyllabusId) {
        return attendanceRepository.findAllBySyllabus_Id(SyllabusId);
    }
}
