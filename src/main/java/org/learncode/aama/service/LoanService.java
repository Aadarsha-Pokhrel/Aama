package org.learncode.aama.service;

import org.aspectj.weaver.ast.Not;
import org.learncode.aama.Dao.*;
import org.learncode.aama.entites.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LoanService {
    @Autowired
    private LoanRepo loanRepo;
    @Autowired
    private LoanRequestRepo loanRequestRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private NoticeRepo noticeRepo;

    public Notice createLoan(Long userId, LoanRequest loanRequest){
        Optional<Users> user = userRepo.findById(userId);
        Users users = user.get();
        loanRequest.setUsers(users);
        LoanRequest loanreq = loanRequestRepo.save(loanRequest);
        Notice notice= new Notice();
        notice.setType("Loan Request for Rs"+loanRequest.getAmount());
        notice.setPurpose(loanRequest.getPurpose()+"  Status : "+loanRequest.getStatus());
        return notice;

    }

    public Notice approve(Long loanId,Long adminId){
        Users admin = userRepo.getById(adminId);
        if(admin.getRole().equals("ADMIN")){
            Optional<LoanRequest> loanreq = loanRequestRepo.findById(loanId);
            LoanRequest loanRequest = loanreq.get();
            Notice noticeByPurpose = noticeRepo.getNoticeByPurpose(loanRequest.getPurpose() + "  Status : " + loanRequest.getStatus());
            loanRequest.setStatus("Approved");
            loanRequestRepo.save(loanRequest);
            noticeByPurpose.setPurpose(loanRequest.getPurpose() + "  Status : " + loanRequest.getStatus());
            Notice save = noticeRepo.save(noticeByPurpose);
            return save;

        }
        else{
            System.out.println("Only admin can approve request");
            return null;
        }





    }

    public Notice reject(Long loanId,Long adminId){
        Users admin = userRepo.getById(adminId);
        if(admin.getRole().equals("ADMIN")){
            Optional<LoanRequest> loanreq = loanRequestRepo.findById(loanId);
            LoanRequest loanRequest = loanreq.get();
            Notice noticeByPurpose = noticeRepo.getNoticeByPurpose(loanRequest.getPurpose() + "  Status : " + loanRequest.getStatus());
            loanRequest.setStatus("Rejected");
            loanRequestRepo.save(loanRequest);
            noticeByPurpose.setPurpose(loanRequest.getPurpose() + "  Status : " + loanRequest.getStatus());
            Notice save = noticeRepo.save(noticeByPurpose);
            return save;
        }
        else{
            System.out.println("Only admin can approve request");
            return null;
        }



    }


}
