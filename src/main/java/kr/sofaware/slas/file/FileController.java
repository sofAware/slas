package kr.sofaware.slas.file;

import kr.sofaware.slas.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
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
    static int BUFFER_SIZE = 8 * 1024;

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
        if (!Files.exists(path)) {
            response.setStatus(404);
            return;
        }

        // 파일 이름 인코딩
        String ext = path.toString().substring(path.toString().lastIndexOf("."));
        String encodedFilename = URLEncoder.encode(
                filename.orElse("download" + ext),
                StandardCharsets.UTF_8);

        byte[] buf = new byte[BUFFER_SIZE];

        // 비디오 스트리밍
        if (ext.equals(".mp4")) {
            long rangeStart = 0;
            long rangeEnd = 0;
            boolean isPart = false;

            try (RandomAccessFile randomFile = new RandomAccessFile(path.toFile(), "r")) {
                long movieSize = randomFile.length();
                String range = request.getHeader("range");

                // Request 에서 범위 가져와서 변수 세팅
                if (range == null) {
                    rangeStart = 0;
                    rangeEnd = movieSize - 1;
                } else {
                    if (range.endsWith("-"))
                        range += + (movieSize - 1);

                    String[] rangeSplits = range.substring(6).split("-");
                    rangeStart = Long.parseLong(rangeSplits[0]);
                    rangeEnd = Long.parseLong(rangeSplits[1]);

                    if (rangeStart > 0)
                        isPart = true;
                }

                // 전송 크기
                long partSize = rangeEnd - rangeStart + 1;

                // 전송 시작
                response.reset();
                response.setStatus(isPart ? 206 : 200);
                response.setContentType("video/mp4");
                response.setHeader("Content-Range", "bytes " + rangeStart + "-" + rangeEnd + "/" + movieSize);
                response.setHeader("Accept-Ranges", "bytes");
                response.setHeader("Content-Length", "" + partSize);

                ServletOutputStream sos = response.getOutputStream();
                randomFile.seek(rangeStart);

                do {
                    int block = partSize > BUFFER_SIZE ? BUFFER_SIZE : (int)partSize;
                    int len = randomFile.read(buf, 0, block);
                    sos.write(buf, 0, len);
                    partSize -= block;
                } while (partSize > 0);
            }
        }
        // 파일 다운로드 및 이미지 등
        else {
            // 헤더 헤팅
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setContentLengthLong(path.toFile().length());
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=" +encodedFilename);

            // 전송
            try (RandomAccessFile randomFile = new RandomAccessFile(path.toFile(), "r")) {
                ServletOutputStream sos = response.getOutputStream();
                File file = path.toFile();
                long partSize = file.length();

                do {
                    int block = partSize > BUFFER_SIZE ? BUFFER_SIZE : (int)partSize;
                    int len = randomFile.read(buf, 0, block);
                    sos.write(buf, 0, len);
                    partSize -= block;
                } while (partSize > 0);
            }
        }
    }
}
