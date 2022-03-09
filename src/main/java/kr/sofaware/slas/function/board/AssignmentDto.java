package kr.sofaware.slas.function.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@ToString
@AllArgsConstructor
public class AssignmentDto {
    private String syllabusId;
    private String title;

    private String submitStart;
    private String submitEnd;
    
    private String content;
    private MultipartFile file;
    private String deleteFile;
    
    public Date getSubmitStart() {
        try {
            return new SimpleDateFormat("yyyy-MM-dd")
                    .parse(submitStart);
        } catch (ParseException e) { }
        return new Date(0);
    }

    public Date getSubmitEnd() {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .parse(submitEnd + " 23:59:59");
        } catch (ParseException e) { }
        return new Date(0);
    }
}
