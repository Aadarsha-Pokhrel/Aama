package org.learncode.aama.controllers;


import org.learncode.aama.Dto.depositDto;
import org.learncode.aama.entites.UserPrincipal;
import org.learncode.aama.entites.Users;
import org.learncode.aama.service.DepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins ="http://localhost:5173")
public class depositController {

    @Autowired
    private DepositService depositService;

    @GetMapping("/deposits")
    public depositDto getdeposits(){
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
        Users user = principal.getUser();

        depositDto deposit = depositService.getDeposits(user.getUserID());


        return deposit;

    }

}
