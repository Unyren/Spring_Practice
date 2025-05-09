package orz.zerock.ex3.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import orz.zerock.ex3.dto.SampleDTO;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/sample")
@Log4j2
public class SampleController {
    @GetMapping("/ex1")
    public void ex1() {
        log.info("ex1...");
    }

    @GetMapping({"/ex2","/exLink"})
    public void exModel(Model model) {
        List<SampleDTO> list= IntStream.rangeClosed(1,20).asLongStream().mapToObj( i->{
            SampleDTO dto=SampleDTO.builder()
                    .sno(i)
                    .first("first..."+i)
                    .last("last..."+i)
                    .regTime(LocalDateTime.now())
                    .build();
            return dto;
        }).collect(Collectors.toList());

        model.addAttribute("list",list);
    }

    @GetMapping("/exInline")
    public String exInline(RedirectAttributes rttr) {
            SampleDTO dto=SampleDTO.builder()
                    .sno(100L)
                    .first("first...100")
                    .last("last...100")
                    .regTime(LocalDateTime.now())
                    .build();
        rttr.addFlashAttribute("result","success");
        rttr.addFlashAttribute("dto",dto);

        return "redirect:/sample/ex3";

    }
    @GetMapping("/ex3")
    public void ex3(Model model) {
    }

    @GetMapping({"/exLayout1","/exLayout2","/exTemplate","/exSidebar"})
    public void exLayout1() {
        log.info("exLayout1...");
    }

}
