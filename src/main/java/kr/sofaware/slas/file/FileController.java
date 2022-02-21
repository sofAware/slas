package kr.sofaware.slas.file;

import kr.sofaware.slas.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Controller
@RequestMapping("/upload")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/**")
    @ResponseBody
    public String upload(HttpServletRequest request,
                         @RequestParam("file") MultipartFile file) throws IOException {
        String syllabusId = request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/") + 1);
        return fileService.saveOnSyllabus(file, syllabusId);
    }

    @GetMapping("/**")
    public void download(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam("filename") Optional<String> filename) throws IOException {

        // 파일 위치 세팅 후 유무 체크
        Path path = Paths.get(System.getProperty("user.dir"), request.getRequestURI());
        if (!Files.exists(path)) return;

        // 파일 이름 인코딩
        String encodedFilename = URLEncoder.encode(
                filename.orElse("download" + path.toString().substring(path.toString().lastIndexOf("."))),
                StandardCharsets.UTF_8);

        // 헤더 헤팅
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setContentLengthLong(path.toFile().length());
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=" +encodedFilename);
        ServletOutputStream sos = response.getOutputStream();

        // 전송
        sos.write(Files.readAllBytes(path));
    }
}
