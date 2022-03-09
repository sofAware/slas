package kr.sofaware.slas.function.board;

import kr.sofaware.slas.entity.Board;
import kr.sofaware.slas.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@Getter
@AllArgsConstructor
public class AssignmentSubmitInfo {

    private Member member;
    private Optional<Board> board;
}
