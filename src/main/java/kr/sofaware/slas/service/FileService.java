package kr.sofaware.slas.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {
    private final String PATH_ROOT = "upload";
    private final String PATH_SYLLABUS = "syllabus";

    private List<Path> deletingPaths = new ArrayList<>();

    /**
     * file type의 input 태그에서 파일을 받은 MultipartFile 을 uuid 값을 생성하여 저장해줍니다.
     * @param file 저장할 파일
     * @param syllabusId 학정번호
     * @author 양경호
     * @return 저장된 파일 위치
     * @throws IOException
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
        if (!Files.exists(dirPath))
            Files.createDirectory(dirPath);
        file.transferTo(filePath);

        // 경로 반환
        String p = filePath.toString().replaceAll("\\\\", "/");
        return p.substring(p.lastIndexOf("/upload/"));
    }

    /**
     * 학정번호와 파일명을 토대로 파일 삭제
     * @param path
     * @author 양경호
     */
    public void deleteOnSyllabus(String path) {

        // 경로가 강의 관련 파일이 아닐 경우 나가리
        if (!path.startsWith(String.format("/%s/%s/", PATH_ROOT ,PATH_SYLLABUS)))
            return;

        // 루트 위치 정하기
        String workPath = System.getProperty("user.dir");

        //            중간위치   학정번호              uuid
        // .../upload/syllabus/21-1-0201-1-0001-01/41bad8f9-b5be-4ef0-991a-79d008556d80.png 와 같은 형식으로 저장
        Path filePath = Paths.get(workPath, path);

        // 파일이 없을 경우 패스
        if (!Files.exists(filePath))
            return;

        // 이미 삭제중일 경우 패스
        if (existDeletingFile(filePath))
            return;

        // 파일이 사용중일 경우가 있어서 삭제중이라고 리스트에 올려놓고 10초마다 삭제 시도
        deletingPaths.add(filePath);
        new Thread(() -> {
            try {
                while (Files.exists(filePath)) {
                    System.out.println("FileService.deleteOnSyllabus | trying to delete file " + filePath);
                    try { Files.delete(filePath); }
                    catch (FileSystemException e) { }
                    Thread.sleep(10000);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            } finally {
                deletingPaths.remove(filePath);
            }
        }).start();
    }

    public boolean existDeletingFile(Path path) {
        return deletingPaths.contains(path);
    }
}
