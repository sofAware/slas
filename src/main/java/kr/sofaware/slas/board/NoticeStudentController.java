package kr.sofaware.slas.board;

import kr.sofaware.slas.entity.Syllabus;
import kr.sofaware.slas.service.BoardService;
import kr.sofaware.slas.service.LectureService;
import kr.sofaware.slas.entity.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;
import java.util.function.Function;

@Controller
@RequestMapping("/s")
@RequiredArgsConstructor
public class NoticeStudentController {
    private final BoardService noticeService;
    private final LectureService lectureService;

    // 전체 공지사항 리스트
    @GetMapping("notice")
    public String readList(Model model, Principal principal,
                           @Nullable @RequestParam("year-semester") String yearSemester,
                           @Nullable @RequestParam("syllabus-id") String syllabusId) {

        // 사용자의 수강 학기와 과목들 조회
        Map<String, List<Syllabus>> lectures = lectureService.mapAllByStudentId(principal.getName());

        // 학기, 과목 선택 없는 요청이면 제일 최근 학기와 사전 순 빠른 학정번호를 선택
        if (syllabusId == null) {
            if (yearSemester == null) {
                ArrayList<String> yearSemesters = new ArrayList<>(lectures.keySet());
                // 이 사람이 들었던 수업이 없을 경우 그냥 리턴
                if (yearSemesters.isEmpty())
                    return "notice/sNotice";

                yearSemester = yearSemesters.get(0);
            }

            // 첫번째 과목
            syllabusId = lectures.get(yearSemester).get(0).getId();
        } else if (yearSemester == null) {
            yearSemester = syllabusId.substring(0, 4);
        }

        // 21-1를 2021학년도 1학기로 포맷하는 맵 초기화
        Map<String, String> formatYS = new TreeMap<>(Collections.reverseOrder());
        lectures.keySet().forEach(s -> formatYS.put(s, Syllabus.formatYearSemester(s)));

        // 학기 선택 리스트
        model.addAttribute("mapYS", formatYS);
        model.addAttribute("yearSemester", Syllabus.formatYearSemester(yearSemester));

        // 강의 선택 리스트
        model.addAttribute("syllabuses", lectures.get(yearSemester));
        String finalSyllabusId = syllabusId;
        model.addAttribute("selectedSyllabusName",
                lectures.get(yearSemester).stream().filter(s -> s.getId().equals(finalSyllabusId))
                        .findAny().get().getName());

        // 게시판 목록 긁어오기
        List<Board> boards = noticeService.listAll(syllabusId);
        model.addAttribute("boards", boards);

        return "notice/sNotice";
    }

    // 열람
    @GetMapping("notice/{boardIdStr:[1-9]+}")
    @ResponseBody
    public String view(Model model,
                       @PathVariable String boardIdStr) {
        int boardId = Integer.parseInt(boardIdStr);

        return boardIdStr + "번의 게시글을 조회하려 하는군요 아직 개발중이니 기다려 주시오.";
    }
}
