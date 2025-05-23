package org.zerock.board.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;
import org.zerock.board.repository.BoardRepository;
import org.zerock.board.repository.ReplyRepository;

import java.util.function.Function;

@RequiredArgsConstructor
@Service
@Log4j2
public class BoardServiceImpl implements BoardService {

    private final BoardRepository repository;
    private final ReplyRepository replyRepository;//자동주입 final
    @Override
    public Long register(BoardDTO dto) {
        log.info(dto);

        Board board = dtoToEntity(dto);

        repository.save(board);



        return board.getBno();
    }

    @Override
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {

        log.info(pageRequestDTO);

        Function<Object[],BoardDTO> fn=(en->entityToDto((Board) en[0],(Member)en[1],(Long)en[2]));

        Page<Object[]> result=repository.searchPage(
                pageRequestDTO.getType(),
                pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("bno").descending()));

        return new PageResultDTO<>(result,fn);
    }

    @Override
    public BoardDTO get(Long bno) {
        Object result=repository.getBoardByBno(bno);
        Object[] arr=(Object[])result;
        return entityToDto((Board)arr[0],(Member)arr[1],(Long)arr[2]);
    }
    @Transactional
    @Override
    public void removeWithReplies(Long bno) {
        replyRepository.deleteByBno(bno);//댓글선삭제

        repository.deleteById(bno);//댓글 선삭제후 게시글 삭제
    }
    @Transactional
    @Override
    public void modify(BoardDTO dto) {
        Board board=repository.getReferenceById(dto.getBno());

        if(board!=null) {
            board.chageTitle(dto.getTitle());
            board.chageContent(dto.getContent());
            repository.save(board);
        }
    }
}
