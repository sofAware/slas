//package kr.sofaware.slas.repository;
//
//import kr.sofaware.slas.entity.Board;
//import kr.sofaware.slas.entity.Comment;
//import kr.sofaware.slas.entity.Member;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//import java.util.Random;
//
//@SpringBootTest
//class BoardRepositoryTest {
//
//    @Autowired private MemberRepository memberRepository;
//    @Autowired private BoardRepository boardRepository;
//    @Autowired private CommentRepository commentRepository;
//
//    Random rand = new Random();
//
//    @Test
//    void addBoardAndViewTest() {
//        // 아무 사람 한명 뽑고
//        List<Member> members = memberRepository.findAll();
//        Member member = members.get(rand.nextInt(members.size()));
//
//        // 게시글 객체 생성
//        Board saveBoard = Board.builder()
//                .category(1)
//                .title("제목이에용")
//                .content("내용입니당")
//                .member(member)
//                .date(new Date())
//                .view(rand.nextInt(900) + 100)
//                .build();
//
//        System.out.println("boardId before save: " + saveBoard.getId());
//        boardRepository.save(saveBoard);
//        System.out.println("boardId after save: " + saveBoard.getId());
//        System.out.println("saveBoard = " + saveBoard);
//        // 저장 후 번호가 자동 갱신되는 것들 볼 수 있었음.
//        // 트랜잭션 넣어뒀는데도 항상 증가되어 누적되고 있긴함.
//
//        Board findBoard = boardRepository.findById(saveBoard.getId()).get();
//        System.out.println("findBoard = " + findBoard);
//
//        Assertions.assertThat(saveBoard).isEqualTo(findBoard);
//    }
//
//    @Test
//    public void findAllByMember_Id() {
//        List<Board> boards = boardRepository.findAllByMember_Id("123");
//        boards.forEach(board -> System.out.println("board = " + board));
//    }
//
//    @Test
//    public void findAllByCategoryAndSyllabus_Id() {
//        List<Board> boards = boardRepository.findAllByCategoryAndSyllabus_Id(1, "21-2-0101-3-0001-01");
//        boards.forEach(board -> System.out.println("board = " + board));
//    }
//
//    @Test
//    public void addCommentTest() {
//        Board board = boardRepository.findById(1).get();
//
//        board.getComments().add(commentRepository.findById(1).get());
//        board.getComments().add(commentRepository.findById(2).get());
//
//        boardRepository.save(board);
//    }
//}