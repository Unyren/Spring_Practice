package org.zerock.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.entity.Board;
import org.zerock.board.service.BoardService;

@Controller
@RequestMapping("/board/*")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO,
                       Model model) {
        model.addAttribute("result", boardService.getList(pageRequestDTO));

        String [] trClass={
                "table-info",
                "table-danger",
                "table-warning",
                "table-success",
                "table-primary"

        };
        model.addAttribute("trClass",trClass);
    }
    @GetMapping("/register")
    public void register() {

    }
    @PostMapping("/register")
    public String registerPost(BoardDTO dto, RedirectAttributes rttr) {
        Long bno= boardService.register(dto);
        rttr.addFlashAttribute("msg",bno);
        return "redirect:/board/list";
    }
    @GetMapping({"/read","/modify"})
    public void read(@ModelAttribute("requestDTO")PageRequestDTO pageRequestDTO
            ,Model model,@RequestParam("bno") Long bno) {
        BoardDTO boardDTO = boardService.get(bno);

        model.addAttribute("dto",boardDTO);
    }
    @PostMapping("/remove")
    public String remove(Long bno, RedirectAttributes rttr) {
        boardService.removeWithReplies(bno);//댓글선삭제후 게시글 삭제
        rttr.addFlashAttribute("msg",bno);
        return "redirect:list";

    }
    @PostMapping("/modify")
    public String modify(BoardDTO dto,@ModelAttribute("requestDTO") PageRequestDTO requestDTO,RedirectAttributes rttr) {
        boardService.modify(dto);
        rttr.addAttribute("page",requestDTO.getPage());
        rttr.addAttribute("type",requestDTO.getType());
        rttr.addAttribute("keyword",requestDTO.getKeyword());
        rttr.addAttribute("bno",dto.getBno());
        return "redirect:/board/read";


    }
}
