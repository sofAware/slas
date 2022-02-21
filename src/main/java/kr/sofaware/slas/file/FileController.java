package kr.sofaware.slas.file;

import kr.sofaware.slas.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.Option;
import java.io.IOException;
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
    @ResponseBody
    public byte[] download(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam("filename") Optional<String> filename) throws IOException {

        Path path = Paths.get(System.getProperty("user.dir"), request.getRequestURI());
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader("Content-Disposition",
                filename.orElse(path.toString().substring(path.toString().lastIndexOf("/") + 1)));

        return Files.exists(path) ? Files.readAllBytes(path) : null;
    }
}
