package kr.sofaware.slas.function.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@ToString
@AllArgsConstructor
public class LectureVideoDto {
    private String syllabusId;
    private String title;

    private int week;
    private int time;

    private String attendanceStart;
    private String attendanceEnd;

    private MultipartFile file;
    private String deleteFile;

    public String getId() {
        return week + "-" + time;
    }

    public Date getAttendanceStart() {
        try {
            return new SimpleDateFormat("yyyy-MM-dd")
                    .parse(attendanceStart);
        } catch (ParseException e) { }
        return new Date(0);
    }

    public Date getAttendanceEnd() {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .parse(attendanceEnd + " 23:59:59");
        } catch (ParseException e) { }
        return new Date(0);
    }
}
