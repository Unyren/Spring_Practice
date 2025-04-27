package org.zerock.mreview.controller;

import jakarta.persistence.ManyToOne;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.mreview.dto.MovieDTO;
import org.zerock.mreview.dto.MovieImageDTO;
import org.zerock.mreview.dto.PageRequestDTO;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.MovieImage;
import org.zerock.mreview.service.MovieService;

import java.io.File;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/movie")
@Log4j2
@RequiredArgsConstructor //객체를 만들어줌
public class MovieController {

    @Value("${org.zerock.upload.path}")//application.properties의 변수
    private String uploadPath;

    private final MovieService movieService;



    //templates/movie/register.html
    @GetMapping("/register")
    public String register() {
        return "movie/register";
    }
    //@GetMapping("/register")
    /* public void register() {

     }*/
    @PostMapping("/register")
    public String register (MovieDTO movieDTO, RedirectAttributes rttr) {
        log.info(movieDTO);

        Long mno= movieService.register(movieDTO);//영화 및 이미지 저장 ManyToOne

        rttr.addFlashAttribute("msg", mno);//list.html에 msg이름의 변수로 게시글번호(mno)값을 전달

        return "redirect:list"; //http://localhost:8080/movie/list GET 방식
    }

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model) {
        log.info("pageRequestDTO: "+pageRequestDTO);
        model.addAttribute("result",movieService.getList(pageRequestDTO));
    }

    @GetMapping({"/read","/modify"})
    public void read(Long mno, @ModelAttribute("requestDTO")PageRequestDTO requestDTO, Model model) {

        MovieDTO movieDTO=movieService.getMovie(mno);
        model.addAttribute("dto",movieDTO);
    }
    @PostMapping("/modify")
    public String modify (MovieDTO movieDTO, @ModelAttribute("requestDTO") PageRequestDTO requestDTO,RedirectAttributes rttr) {
        log.info(movieDTO);


        movieService.modify(movieDTO);//영화 및 이미지 저장 ManyToOne


        rttr.addAttribute("page",requestDTO.getPage());
        rttr.addAttribute("type",requestDTO.getType());
        rttr.addAttribute("keyword",requestDTO.getKeyword());
        rttr.addAttribute("mno",movieDTO.getMno());
        return "redirect:read";
    }
    @PostMapping("/remove")
    public String remove(Long mno, RedirectAttributes rttr) {
        movieService.removeWithMovieAll(mno);//1.MovieImage,2.Review삭제후 /3.Movie삭제
        rttr.addFlashAttribute("msg",mno);
        return "redirect:list";

    }
//fileName="2025/04/03/uuid_파일명"
    public void removeFile(String fileName) {
        String srcFileName=null;
        try {
            srcFileName= URLDecoder.decode(fileName,"UTF-8");

            File file=new File(uploadPath+File.separator+srcFileName);
            boolean result=file.delete();//파일삭제

            //파일에 mime 값 가져오기
            Path source= Paths.get(uploadPath+File.separator+srcFileName);
            String mimeType= Files.probeContentType(source);
            //이미지 파일경우만 썸네일파일 삭제
            if (mimeType.startsWith("image")==true) {
                File thumbnail=new File(file.getParent(),"thumb_"+file.getName());
                result=thumbnail.delete();//썸네일 파일삭제
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
