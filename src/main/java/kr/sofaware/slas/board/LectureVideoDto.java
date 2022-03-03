package kr.sofaware.slas.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Data
@ToString
@AllArgsConstructor
public class LectureVideoDto {
    private String syllabusId;
    private String title;
    private MultipartFile file;
    private String deleteFile;
}
