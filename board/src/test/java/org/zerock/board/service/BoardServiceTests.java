package org.zerock.board.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;

@SpringBootTest
public class BoardServiceTests {
    @Autowired
    private BoardService boardService;


    public void testModify(){
        BoardDTO boardDTO=BoardDTO.builder()
                .bno(10L)
                .title("제목 변경합니다.")
                .content("내용 변경합니다.")
                .build();
        boardService.modify(boardDTO);
    }

    public void testRemove(){
        Long bno=98L;
        boardService.removeWithReplies(bno);
    }
    @Test
    public void testGet(){
        Long bno= 10L;
        BoardDTO boardDTO = boardService.get(bno);
        System.out.println(boardDTO);
    }
    public void testList(){
        PageRequestDTO pageRequestDTO=new PageRequestDTO();

        PageResultDTO<BoardDTO, Object[]> result=boardService.getList(pageRequestDTO);

        for(BoardDTO boardDTO:result.getDtoList()){
            System.out.println(boardDTO);
        }
    }
    public void testRegister() {
        BoardDTO dto = BoardDTO.builder()
                .title("Test.")
                .content("test...")
                .writerEmail("user...96@gmail.com")
                .build();

        Long bno=boardService.register(dto);
    }
}
