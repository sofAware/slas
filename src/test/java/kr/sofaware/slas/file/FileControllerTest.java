//package kr.sofaware.slas.file;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class FileControllerTest {
//
//    @Test
//    void upload() throws IOException {
//        // 파일명으로 쓸 UUID 만들고
//        String uuid = UUID.randomUUID().toString();
//
//        // 파일 확장자 가져오고
//        String ext = ".png";
//
//        // 루트 위치 정하기
//        String workPath = System.getProperty("user.dir");
//        String datePath = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDateTime.now());
//
//        // .../resources/upload/2022/02/20/41bad8f9-b5be-4ef0-991a-79d008556d80.png 와 같은 형식으로 저장
//        Path path = Paths.get(workPath, "upload", datePath, uuid + ext);
//    }
//}