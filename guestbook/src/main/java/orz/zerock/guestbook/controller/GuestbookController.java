package orz.zerock.guestbook.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import orz.zerock.guestbook.dto.GuestbookDTO;
import orz.zerock.guestbook.dto.PageRequestDTO;
import orz.zerock.guestbook.service.GuestbookService;

@Controller
@RequestMapping("/guestbook/*")
@Log4j2
@RequiredArgsConstructor
public class GuestbookController {

    private final GuestbookService service;//의존성 자동 주입

    @GetMapping("/list")
    public String list(PageRequestDTO pageRequestDTO, Model model) {
        log.info("list");
        model.addAttribute("result",service.getlist(pageRequestDTO));
        return "guestbook/list";
    }

    //등록화면
    @GetMapping("/register")
    public void register() {

    }

    //등록처리
    @PostMapping("register")
    public String registerPost(GuestbookDTO dto, RedirectAttributes rttr) {
        Long gno=service.register(dto); //방명록 등록
        rttr.addFlashAttribute("msg",gno);
        return "redirect:/guestbook/list";
    }

    //방명록 조회
    @GetMapping({"/read","/modify"})
    public void read(Long gno, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model) {
        GuestbookDTO dto = service.read(gno);
        //모델을 이용해서 dto 객체를  View 전달
        model.addAttribute("dto",dto);
    }

    @PostMapping("/modify")
    public String modify(GuestbookDTO dto,
                         @ModelAttribute("requestDTO") PageRequestDTO requestDTO
                        ,RedirectAttributes rttr) {

        log.info("post modify..................................................");
        log.info("dto: "+dto);

        service.modify(dto);

        rttr.addAttribute("page",requestDTO.getPage());
        rttr.addAttribute("type",requestDTO.getType());
        rttr.addAttribute("keyword",requestDTO.getKeyword());
        rttr.addAttribute("gno",dto.getGno());

        return "redirect:/guestbook/read";
    }

    @PostMapping("/remove")
    public String remove(Long gno, RedirectAttributes rttr) {
        service.remove(gno);
        rttr.addFlashAttribute("msq",gno);

        return "redirect:/guestbook/list";

    }
    @GetMapping("/like")
    @ResponseBody
    public void like(Long gno, RedirectAttributes rttr) {
        log.info("gno: "+gno);
        service.like(gno);
    }
}
