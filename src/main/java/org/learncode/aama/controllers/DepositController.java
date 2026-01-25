package org.learncode.aama.controllers;

import org.learncode.aama.entites.Deposit;
import org.learncode.aama.entites.UserPrincipal;
import org.learncode.aama.entites.Users;
import org.learncode.aama.service.DepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins ="http://localhost:5173")

public class DepositController {

    @Autowired
    private DepositService depositService;

    @PostMapping("/deposit")
    public Deposit createDeposit(@RequestBody Deposit deposit) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
        Users user = principal.getUser();

        return depositService.createDeposit(user.getUserID(), deposit);
    }

    @GetMapping("/deposits/user")
    public List<Deposit> getDepositsByUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
        Users user = principal.getUser();

        return depositService.getDepositsByUserId(user.getUserID());
    }


    @GetMapping("/deposits/{depositId}")
    public Deposit getDepositById(@PathVariable("depositId") Long depositId) {
        return depositService.getDepositById(depositId);
    }

    @GetMapping("/deposits")
    public List<Deposit> getAllDeposits() {
        return depositService.getAllDeposits();
    }


    @PutMapping("/deposits/{depositId}/status")
    public Deposit updateDepositStatus(@PathVariable("depositId") Long depositId,
                                       @RequestParam("status") String status) {
        return depositService.updateDepositStatus(depositId, status);
    }
}
