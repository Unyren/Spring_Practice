package orz.zerock.ex1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RestController를 이용해서 별도의 화면없이 데이터를 전송
public class SampleController {
    @GetMapping("/hello")
    public String[] hello() {//hello()는 @GetMapping을 이용하여 브라우저 주소창에서 호출이 가능
        return new String[]{"Hello","World"};
    }
}