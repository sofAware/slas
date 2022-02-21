package kr.sofaware.slas.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {
    private final String PATH_ROOT = "upload";
    private final String PATH_SYLLABUS = "syllabus";

    /**
     * file type의 input 태그에서 파일을 받은 MultipartFile 을 uuid 값을 생성하여 저장해줍니다.
     * @param file 저장할 파일
     * @param syllabusId 학정번호
     * @author 양경호
     * @return 저장된 파일 위치
     */
    public String saveOnSyllabus(MultipartFile file, String syllabusId) throws IOException {

        // 빈 파일 나가리
        if (file.getOriginalFilename().isEmpty())
            return "";

        // 파일명으로 쓸 UUID 만들고
        String uuid = UUID.randomUUID().toString();

        // 파일 확장자 가져오고
        String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

        // 루트 위치 정하기
        String workPath = System.getProperty("user.dir");

        //            중간위치   학정번호              uuid
        // .../upload/syllabus/21-1-0201-1-0001-01/41bad8f9-b5be-4ef0-991a-79d008556d80.png 와 같은 형식으로 저장
        Path dirPath = Paths.get(workPath, PATH_ROOT, PATH_SYLLABUS, syllabusId);
        Path filePath = Paths.get(dirPath.toString(), uuid + ext);

        // 폴더가 없을 경우 쭉 만들고 파일 저장
        dirPath.toFile().mkdirs();
        file.transferTo(filePath);

        // 경로 반환
        String p = filePath.toString().replaceAll("\\\\", "/");
        return p.substring(p.lastIndexOf("/upload/"));
    }
}
