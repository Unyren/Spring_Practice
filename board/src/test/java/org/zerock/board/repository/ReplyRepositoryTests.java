package org.zerock.board.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;
import org.zerock.board.entity.Reply;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class ReplyRepositoryTests {
    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void testListByBoard() {
        List<Reply> replyList=replyRepository.getRepliesByBoardOrderByRno(Board.builder()
                        .bno(97L).build());
        replyList.forEach(System.out::println);
    }
    public void readReply1() {
        Optional<Reply> result = replyRepository.findById(1L);

        Reply reply = result.get();

        System.out.println(reply);
        System.out.println(reply.getBoard());
    }
    public void insertReply() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Long bno= (long) (Math.random()*100)+ 3;

            Board board = Board.builder().bno(bno).build();

            Reply reply = Reply.builder()
                    .text("Reply..."+i)
                    .board(board)
                    .replyer("guest")
                    .build();

            System.out.println(replyRepository.save(reply));
        });
    }
}
