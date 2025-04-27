package org.zerock.guestbook2.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

//콘트롤러에 들어가야할 어노테이션
@Controller
// /guestbook 요청시 GuestbookController 호출
@RequestMapping("/guestbook/*")
//로그
@Log4j2
public class GuestbookController {

    @GetMapping("/list")
    public String list() {
        log.info("list");
        return "guestbook/list";
    }
}

