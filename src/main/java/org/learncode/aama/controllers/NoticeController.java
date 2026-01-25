package org.learncode.aama.controllers;


import jakarta.servlet.http.HttpSession;
import org.learncode.aama.entites.Notice;
import org.learncode.aama.entites.UserPrincipal;
import org.learncode.aama.entites.Users;
import org.learncode.aama.service.noticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins ="http://localhost:5173")
public class NoticeController {
    @Autowired
    private noticeService service;

    @PostMapping("/create-notice")
    public List<Notice> postNotice(@RequestBody Notice notice){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
        Users user = principal.getUser();
        Notice notice1 = service.createNotice(notice,user.getUserID());
        return List.of(notice1);

    }

    @GetMapping("/notice" )
    public List<Notice> getNotice(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
        Users user = principal.getUser();

        List<Notice> notice = service.getNotice(user.getUserID());
        return notice;
    }


}
