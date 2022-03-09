package kr.sofaware.slas.service;

import kr.sofaware.slas.function.board.AssignmentSubmitInfo;
import kr.sofaware.slas.entity.Board;
import kr.sofaware.slas.entity.Member;
import kr.sofaware.slas.repository.BoardRepository;
import kr.sofaware.slas.repository.LectureRepository;
import kr.sofaware.slas.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssignmentSubmitService implements BoardService {

    private final BoardRepository boardRepository;
    private final LectureRepository lectureRepository;
    private final MemberRepository memberRepository;

    @Override
    public void save(Board board) {
        board.setCategory(Board.CATEGORY_ASSIGNMENT_SUBMIT);
        board.setDate(new Date());
        boardRepository.save(board);
    }

    @Override
    public Optional<Board> read(int boardId) {
        return boardRepository.findById(boardId);
    }

    @Override
    public void delete(int boardId) {
        boardRepository.deleteById(boardId);
    }

    @Override
    public List<Board> listAll(String SyllabusId) {
        return boardRepository.findAllByCategoryAndSyllabus_Id(
                Board.CATEGORY_ASSIGNMENT_SUBMIT,
                SyllabusId);
    }

    @Override
    public void increaseViewCount(int boardId) {

    }

    public Optional<Board> findByAssignment_IdAndMember_Id(int assignmentId, String studentId) {
        return boardRepository.findByAssignment_IdAndMember_Id(assignmentId, studentId);
    }

    /**
     * 해당 수업을 듣는 모든 학생들 정보 + 제출한 파일이 있으면 그것 까지 반환
     * @param syllabusId 학정번호
     * @return 맴버와 제출게시글 세트 리스트
     */
    public List<AssignmentSubmitInfo> listSubmitInfo(String syllabusId) {
        // 수업 듣는 학생들 가져오기
        List<Member> members = lectureRepository
                .findAllBySyllabus_Id(syllabusId)
                .stream()
                .map(lecture -> memberRepository.findById(lecture.getStudent().getId()).get())
                .collect(Collectors.toList());

        // 학생들 정보로 학생과 제출 세트들 만들기
        return members.stream().map(member ->
                new AssignmentSubmitInfo(member, listAll(syllabusId).stream().filter(board ->
                        board.getMember().getId().equals(member.getId())).findFirst())
        ).collect(Collectors.toList());
    }
}
