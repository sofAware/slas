package kr.sofaware.slas.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class FileService {

    /***
     * file type의 input 태그에서 파일을 받은 MultipartFile 을 오늘 날짜와 uuid 값을 생성하여 저장해줍니다.
     * @param file 저장할 파일
     * @return 파일 위치
     */
    public String save(MultipartFile file) throws IOException {
        // 빈 파일 나가리
        if (file.getOriginalFilename().isEmpty())
            return "";

        // 파일명으로 쓸 UUID 만들고
        String uuid = UUID.randomUUID().toString();

        // 파일 확장자 가져오고
        String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

        // 루트 위치 정하기
        String workPath = System.getProperty("user.dir");
        String datePath = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDateTime.now());

        // .../resources/upload/2022/02/20/41bad8f9-b5be-4ef0-991a-79d008556d80.png 와 같은 형식으로 저장
        Path dirPath = Paths.get(workPath, "upload", datePath);
        Path filePath = Paths.get(dirPath.toString(), uuid + ext);

        // 폴더가 없을 경우 쭉 만들고 파일 저장
        dirPath.toFile().mkdirs();
        file.transferTo(filePath);

        // 경로 반환
        String p = filePath.toString().replaceAll("\\\\", "/");
        return p.substring(p.lastIndexOf("/upload/"));
    }
}
