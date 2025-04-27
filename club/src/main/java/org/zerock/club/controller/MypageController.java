package org.zerock.club.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mypage/*")
@Log4j2
public class MypageController {

    @GetMapping({"", "/"})
    public String main(){
        log.info("Mypage Main");
        return "mypage/index";
    }

}
