package kr.sofaware.slas.auth.service;

import kr.sofaware.slas.repository.MemberRepository;
import kr.sofaware.slas.repository.BoardRepository;
import org.junit.jupiter.api.Test;

class MemberServiceTest {
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    public MemberServiceTest(MemberRepository memberRepository, BoardRepository boardRepository) {
        this.memberRepository = memberRepository;
        this.boardRepository = boardRepository;
    }

    @Test
    public void foreignKeyTest() {

    }
}