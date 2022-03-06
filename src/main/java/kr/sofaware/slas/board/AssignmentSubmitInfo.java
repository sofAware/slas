package kr.sofaware.slas.board;

import kr.sofaware.slas.entity.Board;
import kr.sofaware.slas.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@Getter
@AllArgsConstructor
public class AssignmentSubmitInfo {

    Member member;
    Optional<Board> board;
}
