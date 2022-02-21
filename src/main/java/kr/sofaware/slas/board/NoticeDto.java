package kr.sofaware.slas.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Data
@ToString
@AllArgsConstructor
public class NoticeDto {
    private String syllabusId;
    private String title;
    private String content;
    private MultipartFile file;
    private String deleteFile;
}
