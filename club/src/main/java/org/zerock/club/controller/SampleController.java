package org.zerock.club.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zerock.club.security.dto.ClubAuthMemberDTO;

@Controller
@Log4j2
@RequestMapping("/sample/")//http://localhost:8080/sample/
public class SampleController {
    @PreAuthorize("permitAll()")//모든 사용자가 접근 가능
    @GetMapping("/all")
    public void exAll() {
        log.info("exAll");
        // /resources/templates/sample/all.html
    }
    @GetMapping("/member")
    public void exMember(@AuthenticationPrincipal ClubAuthMemberDTO clubAuthMember) {
        log.info("exMember");
        log.info("[DEG] "+clubAuthMember);
        // /resources/templates/sample/member.html
    }
    @PreAuthorize("hasRole('ADMIN')")//ROLE_ADMIN 사용자만 접근 가능
    @GetMapping("/admin")
    public void exAdmin() {
        log.info("exAdmin");
        // /resources/templates/sample/admin.html
    }
    @PreAuthorize("#clubAuthMember!=null && #clubAuthMember.username eq \"user95@zerock.org\"")
    @GetMapping("/exOnly")
    public String exOnly(@AuthenticationPrincipal ClubAuthMemberDTO clubAuthMember) {
        log.info(clubAuthMember);
        return "/sample/admin";
    }
}
