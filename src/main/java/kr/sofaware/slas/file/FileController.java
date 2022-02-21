package kr.sofaware.slas.file;

import kr.sofaware.slas.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
    @ResponseBody
    public byte[] download(HttpServletRequest request) throws IOException {

        Path path = Paths.get(System.getProperty("user.dir"), request.getRequestURI());

        return Files.exists(path) ? Files.readAllBytes(path) : null;
    }
}
