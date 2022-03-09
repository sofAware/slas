package kr.sofaware.slas.repository;

import kr.sofaware.slas.entity.Attendance;
import kr.sofaware.slas.entity.AttendancePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, AttendancePK> {
    Attendance findBySyllabus_IdAndStudent_Id(String syllabus_id, String student_id);

    List<Attendance> findByStudent_Id(String id);
    List<Attendance> findAllByStudent_Id(@Param(value = "memberId") String id);
    List<Attendance> findAllByStudent_IdAndSyllabus_Id(String id, String syllabusId);
    List<Attendance> findAllBySyllabus_Id(@Param(value = "memberId") String syllabusId);
}
