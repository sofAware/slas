package kr.sofaware.slas.function.board;

import kr.sofaware.slas.entity.Board;
import kr.sofaware.slas.entity.Syllabus;
import kr.sofaware.slas.service.BoardService;
import kr.sofaware.slas.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/s/" + LectureFileStudentController.ROOT_URL)
@RequiredArgsConstructor
public class LectureFileStudentController {

    public static final String ROOT_URL = "lf";
    private static final String TITLE = "\uD83E\uDDF7 강의자료";
    private final BoardService lectureFileService;

    private final LectureService lectureService;

    // 전체 공지사항 리스트
    @GetMapping
    public String readList(Model model, Principal principal,
                           @Nullable @RequestParam("year-semester") String yearSemester,
                           @Nullable @RequestParam("syllabus-id") String syllabusId) {

        // 사용자의 수강 학기와 과목들 조회
        Map<String, List<Syllabus>> lectures = lectureService.mapAllByStudentId(principal.getName());

        // 강의 선택 없으면
        if (syllabusId == null || syllabusId.isEmpty()) {
            // 학기 선택도 없으면 최근학기 전체 공지 선택
            if (yearSemester == null || yearSemester.isEmpty()) {
                ArrayList<String> yearSemesters = new ArrayList<>(lectures.keySet());
                // 이 사람이 했던 수업이 없을 경우 그냥 리턴
                if (yearSemesters.isEmpty())
                    return "board/list";

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

        // 강의 선택 없으면 해당 학기 전체 강의에 대한 공지사항 긁어오기
        List<Board> boards = new ArrayList<>();
        if (syllabusId == null || syllabusId.isEmpty()) {
            lectures.get(yearSemester).forEach(syllabus ->
                    boards.addAll(lectureFileService.listAll(syllabus.getId())));

            model.addAttribute("selectedSyllabusId", "");
            model.addAttribute("selectedSyllabusName", "전체");
        }
        else {
            boards.addAll(lectureFileService.listAll(syllabusId));

            // 선택된 강의 lectures에서 찾아서 강의명 입력
            Syllabus syllabus = lectures
                    .get(yearSemester)
                    .stream()
                    .filter(s -> s.getId().equals(syllabusId))
                    .findAny()
                    .get();
            model.addAttribute("selectedSyllabusId", syllabus.getId());
            model.addAttribute("selectedSyllabusName",
                    syllabus.getName() + " (" +
                            syllabus.formatClassTime() + ") - " +
                            syllabus.getProfessor().getName());
        }

        // 날짜 내림차순 정렬 후 모델에 넣기
        boards.sort(Comparator.comparing(Board::getDate).reversed());
        model.addAttribute("boards", boards);
        model.addAttribute("rootURL", ROOT_URL);
        model.addAttribute("title", TITLE);

        return "board/list";
    }

    // 열람
    @GetMapping("/{boardIdStr:[0-9]+}")
    public String view(Model model, Principal principal,
                       @PathVariable String boardIdStr) {

        // 게시글 가져오기
        int boardId = Integer.parseInt(boardIdStr);
        Optional<Board> board = lectureFileService.read(boardId);

        // 없으면 404
        if (board.isEmpty())
            return "error/404";

        // 읽을 권한 없으면 403
        if (!lectureService.existsBySyllabus_IdAndStudent_Id(
                board.get().getSyllabus().getId(),
                principal.getName()))
            return "error/403";

        // 조회 수 증가
        lectureFileService.increaseViewCount(boardId);

        // 열람
        model.addAttribute("rootURL", ROOT_URL);
        model.addAttribute("title", TITLE);
        model.addAttribute("board", board.get());
        return "board/view";
    }
}
