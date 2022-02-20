package kr.sofaware.slas.file;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Controller
@RequestMapping("/upload")
@RequiredArgsConstructor
public class FileController {

    @PostMapping("")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file) throws IOException {
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

    @GetMapping("/**")
    @ResponseBody
    public byte[] download(HttpServletRequest request) throws IOException {

        Path path = Paths.get(System.getProperty("user.dir"), request.getRequestURI());

        return Files.exists(path) ? Files.readAllBytes(path) : null;
    }

}
