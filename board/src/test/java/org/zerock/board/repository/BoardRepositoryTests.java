package org.zerock.board.repository;

import com.querydsl.core.util.MathUtils;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class BoardRepositoryTests {
    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void testSearchPage(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
        Page<Object[]> result =  boardRepository.searchPage("tc","1",pageable);
    }
    public void testSearch1(){
        boardRepository.search1();
    }
    public void testRead3(){
        Object result = boardRepository.getBoardByBno(98L);
        Object[] arr= (Object[])result;
        System.out.println(Arrays.toString(arr));
    }
    public void testWithReplyCount(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
        Page<Object[]> result=boardRepository.getBoardWithReplyCount(pageable);
        result.get().forEach(row->{
            Object[] arr= (Object[])row;
            System.out.println(Arrays.toString(arr));
        });
    }
    public void  testGetBoardWithReply(){
        List<Object[]> result = boardRepository.getBoardWithReply(98L);
        for (Object[] arr : result) {
            System.out.println(Arrays.toString(arr));
        }
    }
    public void testReadWithWriter() {
        Object result = boardRepository.getBoardWithWriter(100L);

        Object[] arr= (Object[])result;

        System.out.println("---------------------------------");
        System.out.println(Arrays.toString(arr));

    }

    @Transactional
    public void testRead1() {
        Optional<Board> result = boardRepository.findById(100L);

        Board board=result.get();

        System.out.println(board);
        System.out.println(board.getWriter().getName());
    }

    public void insertBoard() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Member member= Member.builder().email("user..."+i+"@gmail.com").build();

            Board board = Board.builder()
                    .title("title..."+i)
                    .content("content..."+i)
                    .writer(member)
                    .build();

            System.out.println(boardRepository.save(board));
        });
    }
}
