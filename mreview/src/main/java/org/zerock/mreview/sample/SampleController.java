package org.zerock.mreview.sample;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller

//@RequestMapping은 현재 클래스의 모든 메서드들의 기본적인 URL경로가 된다.
//SampleControlle 클래스를 아래와 같이 '/sample/*' 이라는 경로로 지정했다면 다음과같은 URL은 모두 SampleController에서 처리된다

// /sample/aaa
// /sample/bbb

@Log4j2
@RequestMapping("/sample")
public class SampleController {
    //http://localhost:8080/sample
    @RequestMapping(value = "",method = {RequestMethod.POST,RequestMethod.GET})

    public void basic() {
        log.info("basic..........................");
    }
    //http://localhost:8080/sample/basic
    @RequestMapping(value = "/basic",method = {RequestMethod.POST,RequestMethod.GET})

    public void basic2() {
        log.info("basic2..........................");
    }
    //http://localhost:8080/sample/basicOnlyGet

    @GetMapping("/basicOnlyGet")
    public void basicGet2() {
        log.info("basic get only get...");
    }

    //http://localhost:8080/sample/basic3
    @GetMapping("/basic3")
    public String basic3() {
        log.info("basic3...");
        return "sample/basic3";
    }

    //http://localhost:8080/sample/ex01?name=AAA&age=14
    //보내는 순서 상관없음 객체로 받기때문에.
    @GetMapping("/ex01")
    //public String ex01(String name, int age) {
    public String ex01(SampleDTO sampleDTO) {
        log.info(sampleDTO);
        return "sample/basic3";
    }
    //http://localhost:8080/sample/ex02?name=AAA&age=14
    //http://localhost:8080/sample/ex02?uname=AAA&age=14
    @GetMapping("/ex02")
    public String ex02(@RequestParam("uname") String name,@RequestParam int age) {
        log.info(name+":"+age);
        return "sample/basic3";
    }
    //http://localhost:8080/sample/ex02List?ids=AAA&ids=BBB&ids=CCC
    @GetMapping("/ex02List")
    //ArrayList 객체만 담을수 있다.
    public String ex02List(@RequestParam("ids")ArrayList<String> ids) {
        log.info("isd:"+ids);
        return "sample/basic3";
    }
    //http://localhost:8080/sample/ex02Array?ids=AAA&ids=BBB&ids=CCC
    @GetMapping("/ex02Array")
    public String ex02Array(@RequestParam("ids") String[] ids) {
        log.info("isd:"+ Arrays.toString(ids));
        //tostring을 사용하면 주소값이 나온다.
        return "sample/basic3";
    }
    //http://localhost:8080/sample/ex02Bean?list%5B0%5D.name=AAA&list%5B0%5D.age=14&list%5B1%5D.name=BBB&list%5B1%5D.age=24
    @GetMapping("/ex02Bean")
    public String ex02Bean(SampleDTOList list) {
        log.info("list:"+list);
        return "sample/basic3";
    }

    //@InitBinder
   // public void initBinder(WebDataBinder binder) {
   //     SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
   //     binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(dateFormat, false));
//
    // }

    //http://localhost:8080/sample/ex03?title=제목&dueDate=2025-03-31 11:04

    @GetMapping("/ex03")
    public String ex03(
           TodoDTO todoDTO) {
        log.info("dueDate:"+todoDTO);
        return "sample/basic3";
    }

}
