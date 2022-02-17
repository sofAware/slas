package kr.sofaware.slas.board;

import kr.sofaware.slas.entity.Syllabus;
import kr.sofaware.slas.service.BoardService;
import kr.sofaware.slas.entity.Board;
import kr.sofaware.slas.service.SyllabusService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/p")
@RequiredArgsConstructor
public class NoticeProfessorController {
    private final BoardService noticeService;
    private final SyllabusService syllabusService;

    @GetMapping("notice")
    public String readList(Model model, Principal principal,
                           @Nullable @RequestParam("year-semester") String yearSemester,
                           @Nullable @RequestParam("syllabus-id") String syllabusId) {

        // 사용자의 수강 학기와 과목들 조회
        Map<String, List<Syllabus>> lectures = syllabusService.mapAllByProfessorId(principal.getName());

        // 강의 선택 없으면
        if (syllabusId == null || syllabusId.isEmpty()) {
            // 학기 선택도 없으면 최근학기 전체 공지 선택
            if (yearSemester == null || yearSemester.isEmpty()) {
                ArrayList<String> yearSemesters = new ArrayList<>(lectures.keySet());
                // 이 사람이 했던 수업이 없을 경우 그냥 리턴
                if (yearSemesters.isEmpty())
                    return "notice/pNotice";

                // 있으면 최근 학기 입력
                yearSemester = yearSemesters.get(0);
            }
        }
        // 강의 선택이 있으면 학기는 학정번호에서 따오기
        else if (yearSemester == null) {
            yearSemester = syllabusId.substring(0, 4);
        }

        // 초기화
        Map<String, String> formatYS = new TreeMap<>(Collections.reverseOrder());
        lectures.keySet().forEach(s -> formatYS.put(s, Syllabus.formatYearSemester(s)));

        // 학기 선택 리스트
        model.addAttribute("mapYS", formatYS);
        model.addAttribute("yearSemester", yearSemester);
        model.addAttribute("formatYS", Syllabus.formatYearSemester(yearSemester));

        // 강의 선택 리스트
        model.addAttribute("syllabuses", lectures.get(yearSemester));

        // 게시판 목록 초기화
        List<Board> boards = new ArrayList<>();

        // 강의 선택 없으면 해당 학기 전체 강의에 대한 공지사항 긁어오기
        if (syllabusId == null || syllabusId.isEmpty()) {
            lectures.get(yearSemester).forEach(syllabus ->
                    boards.addAll(noticeService.listAll(syllabus.getId())));

            // 날짜 내림차순 정렬
            boards.sort(Comparator.comparing(Board::getDate).reversed());

            model.addAttribute("selectedSyllabusName", "전체");
        }
        else {
            boards.addAll(noticeService.listAll(syllabusId));

            // 선택된 강의 lectures에서 찾아서 강의명 입력
            String finalSyllabusId = syllabusId;
            model.addAttribute("selectedSyllabusName",
                    lectures.get(yearSemester).stream().filter(s -> s.getId().equals(finalSyllabusId))
                            .findAny().get().getName());
        }

        model.addAttribute("boards", boards);

        return "notice/pNotice";
    }

    @GetMapping("notice/write")
    public String GetWriting(Model model, Principal principal) {


        return "/notice/pWrite";
    }
    @PostMapping("notice/write")
    public String postWriting(Model model, Principal principal) {

        // 작성된 포스트 번호로 뷰 이동
        return "redirect:/p/notice/12345678";
    }

    @GetMapping("notice/edit")
    public String GetEditing(Model model, Principal principal) {
        String data = "# 제목 입니다\\n\\n다름이 아니라 지금 예쁜 에디터를 사용해보려고 하는데\\n\\n뭔가 좀 잘 되는 듯 하면서 왜 잘되지 느낌도 받고 참 모르겠을 제 마음을 전하고 싶습니다.\\n\\n아래가 예제 코드랍니다 공식 사이트에서 복붙 해보죠\\n```javascript\\nconst editor = new toastui.Editor({\\n        el: document.querySelector('#editor'),\\n        previewStyle: 'vertical',\\n        height: '500px',\\n        initialValue: content\\n      });\\n```\\n\\n아하 역따옴표 3개로 코드 시작했을 때 끝내려면 다시 역따옴표 3개 더 입력하고 엔터하며 나가지네요.";

        model.addAttribute("content", data);

        return "/notice/pEdit";
    }
    @PostMapping("notice/edit")
    public String postEditing(Model model, Principal principal) {

        // 수정된 포스트 번호로 뷰 이동
        return "redirect:/p/notice/12345678";
    }
}
