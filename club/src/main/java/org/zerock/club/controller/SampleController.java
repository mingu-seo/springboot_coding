package org.zerock.club.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zerock.club.security.dto.ClubAuthMemberDTO;

@Controller
@Log4j2
@RequestMapping("/sample")
public class SampleController {

    @GetMapping("/all")
    public void exAll() {
        log.info("exAll");
    }

    @RequestMapping("/member")
    public void exMember(@AuthenticationPrincipal ClubAuthMemberDTO clubAuthMemberDTO) {
        log.info("exMember");
        log.info("-------------");
        log.info(clubAuthMemberDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping("/admin")
    public void excAdmin() {
        log.info("excAdmin");
    }

    // 권한상관없이 로그인만 되어 있으면 접근가능
    //@Secured("IS_AUTHENTICATED_FULLY")
//    @PreAuthorize("isAuthenticated()")
    @RequestMapping("/mustLogin")
    public void mustLogin() {

    }
}
