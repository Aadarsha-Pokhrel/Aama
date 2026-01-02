package org.learncode.aama.controllers;

import org.learncode.aama.entites.LoanRequest;
import org.learncode.aama.entites.Notice;
import org.learncode.aama.service.LoanService;
import org.learncode.aama.service.noticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoanController {
    @Autowired
    private LoanService loanService;
    @Autowired
    private noticeService noticeService;

    @PostMapping("/loan-request/{id}")
    public Notice createLoanRequest(@RequestBody LoanRequest loanRequest, @PathVariable("id") Long id){

        Notice loan = loanService.createLoan(id, loanRequest);
        Notice notice = noticeService.createNotice(loan);
        return notice;
    }
    @PostMapping("/approve/{loanid}")

    public Notice approveLoan(@PathVariable("loanid") Long loanid, @RequestParam("adminid") Long adminId){
        adminId=1L;
        Notice approve = loanService.approve(loanid, adminId);
        return approve;

    }
    @PostMapping("/reject/{loanid}")
    public String rejectLoan(@PathVariable("loanid") Long loanid, @RequestParam("adminid") Long adminId){
        adminId=1L;
        loanService.reject(loanid, adminId);
        return "Loan rejected";

    }
}
