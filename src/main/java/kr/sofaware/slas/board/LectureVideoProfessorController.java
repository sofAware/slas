package kr.sofaware.slas.board;

import kr.sofaware.slas.entity.Board;
import kr.sofaware.slas.entity.LectureVideo;
import kr.sofaware.slas.entity.Syllabus;
import kr.sofaware.slas.service.*;
import lombok.RequiredArgsConstructor;
import org.mp4parser.IsoFile;
import org.mp4parser.boxes.iso14496.part12.MovieHeaderBox;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/p")
@RequiredArgsConstructor
public class LectureVideoProfessorController {

    private final MemberService memberService;
    private final LectureVideoService lectureVideoService;
    private final SyllabusService syllabusService;
    private final FileService fileService;

    // 전체 강의영상 리스트
    @GetMapping("lv")
    public String readList(Model model, Principal principal,
                           @Nullable @RequestParam("year-semester") String yearSemester,
                           @Nullable @RequestParam("syllabus-id") String syllabusId) {
        // 교수가 강의한 학기와 과목들 조회
        Map<String, List<Syllabus>> lectures = syllabusService.mapAllByProfessorId(principal.getName());

        // 강의 선택 없으면
        if (syllabusId == null || syllabusId.isEmpty()) {
            // 학기 선택도 없으면 최근학기 전체 공지 선택
            if (yearSemester == null || yearSemester.isEmpty()) {
                ArrayList<String> yearSemesters = new ArrayList<>(lectures.keySet());
                // 이 사람이 했던 수업이 없을 경우 그냥 리턴
                if (yearSemesters.isEmpty())
                    return "notice/list";

                // 있으면 최근 학기 입력
                yearSemester = yearSemesters.get(0);
            }
        }
        // 강의 선택이 있으면 학기는 학정번호에서 따오기
        else if (yearSemester == null) {
            yearSemester = syllabusId.substring(0, 4);
        }
        // 21-1를 2021학년도 1학기로 포맷하는 맵 초기화
        Map<String, String> formatYS = new TreeMap<>(Collections.reverseOrder());
        lectures.keySet().forEach(s -> formatYS.put(s, Syllabus.formatYearSemester(s)));

        // 학기 선택 리스트
        model.addAttribute("mapYS", formatYS);
        model.addAttribute("yearSemester", yearSemester);
        model.addAttribute("formatYS", Syllabus.formatYearSemester(yearSemester));

        // 강의 선택 리스트
        model.addAttribute("syllabuses", lectures.get(yearSemester));

        // 강의 선택 없으면 해당 학기 전체 강의에 대한 강의영상 긁어오기
        List<LectureVideo> lectureVideos = new ArrayList<>();
        if (syllabusId == null || syllabusId.isEmpty()) {
            lectures.get(yearSemester).forEach(syllabus ->
                    lectureVideos.addAll(lectureVideoService.listAll(syllabus.getId())));

            // 템플릿에서 강의명과 강의시간을 표시하기 위해 (isEmpty 판별) 추가
            model.addAttribute("selectedSyllabusId", "");
            model.addAttribute("selectedSyllabusName", "전체");
        }
        else {
            lectureVideos.addAll(lectureVideoService.listAll(syllabusId));

            // 선택된 강의 lectures에서 찾아서 강의명 입력
            Syllabus syllabus = lectures
                    .get(yearSemester)
                    .stream()
                    .filter(s -> s.getId().equals(syllabusId))
                    .findAny()
                    .get();
            model.addAttribute("selectedSyllabusId", syllabus.getId());
            model.addAttribute("selectedSyllabusName",
                    syllabus.getName() + " (" + syllabus.formatClassTime() + ")");
        }

        // 날짜 내림차순 정렬 후 모델에 넣기
        lectureVideos.sort(Comparator.comparing(LectureVideo::getAttendanceStart).reversed());
        model.addAttribute("lectureVideos", lectureVideos);

        return "lectureVideo/list";
    }

    // 열람
    @GetMapping("lv/view")
    public String view(Model model, Principal principal,
                       @RequestParam("syllabus-id") String syllabusId,
                       @RequestParam("id") String id) {

        // 학정번호, 주회차를 입력하지 않았다면 400
        if (syllabusId == null || id == null)
            return "error/400";

        // 강의영상 가져오기
        Optional<LectureVideo> lectureVideo = lectureVideoService.get(syllabusId, id);

        // 없으면 404
        if (lectureVideo.isEmpty())
            return "error/404";

        // 읽을 권한 없으면 403
        if (!syllabusService.existsByIdAndProfessor_Id(
                lectureVideo.get().getSyllabus().getId(),
                principal.getName()))
            return "error/403";

        // 열람
        model.addAttribute("lv", lectureVideo.get());
        return "lectureVideo/view";
    }

    // 작성
    @GetMapping("lv/write")
    public String getWriting(Model model, Principal principal,
                             @Nullable @RequestParam("syllabus-id") String syllabusId) {

        // 학정번호가 넘어왔으면 그걸로 강의 모델에 추가 아니면 교수한 강의 최근 1개 추가
        model.addAttribute("syllabus", syllabusId == null ?
                syllabusService.findFirstByProfessor_IdOrderByIdDesc(principal.getName()).get() :
                syllabusService.findById(syllabusId).get());

        return "/lectureVideo/write";
    }

    @PostMapping("lv/write")
    public String postWriting(LectureVideoDto lectureVideoDto, Model model, Principal principal) throws Exception {

        // 작성 권한 없으면 403
        if (!syllabusService.existsByIdAndProfessor_Id(
                lectureVideoDto.getSyllabusId(),
                principal.getName()))
            return "error/403";

        // 강의 영상 저장
        String attachmentPath = "";
        if (!lectureVideoDto.getFile().isEmpty())
            attachmentPath = fileService.saveOnSyllabus(lectureVideoDto.getFile(), lectureVideoDto.getSyllabusId());

        // 테스트 코드
        System.out.println("lectureVideoDto = " + lectureVideoDto);

        // 강의영상 엔티티 만들기
        LectureVideo lectureVideo = new LectureVideo(
                syllabusService.findById(lectureVideoDto.getSyllabusId()).get(),
                lectureVideoDto.getId(),
                lectureVideoDto.getTitle(),
                lectureVideoDto.getAttendanceStart(),
                lectureVideoDto.getAttendanceEnd(),
                (int)getMediaLength(attachmentPath),
                attachmentPath
        );

        // 강의 영상 작성
        lectureVideoService.save(lectureVideo);

        // 작성된 강의영상 번호로 뷰 이동
        return "redirect:/p/lv/view"
                + "?syllabus-id=" + lectureVideo.getSyllabus().getId()
                + "&id=" + lectureVideo.getId();
    }

    public static long getMediaLength(String path) throws IOException {
        path = Paths.get(System.getProperty("user.dir"), path).toString();
        IsoFile isoFile = new IsoFile(path);
        MovieHeaderBox mhb = isoFile.getMovieBox().getMovieHeaderBox();
        return mhb.getDuration() / mhb.getTimescale();
    }
}
