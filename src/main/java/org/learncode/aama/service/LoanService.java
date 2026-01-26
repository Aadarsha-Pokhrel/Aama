package org.learncode.aama.service;

import org.aspectj.weaver.ast.Not;
import org.learncode.aama.Dao.*;
import org.learncode.aama.entites.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    @Transactional
    public Notice createLoan(Long userId, LoanRequest loanRequest){
        Users users = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Use the OneToOne relationship to check if user has a PENDING loan request
        LoanRequest existingLoanRequest = users.getLoanRequest();

        if (existingLoanRequest != null && "pending".equalsIgnoreCase(existingLoanRequest.getStatus())) {
            throw new IllegalStateException("User already has a pending loan request. Please wait for approval or rejection.");
        }

        LoanRequest savedLoanRequest;

        // If existing loan request is not pending (Approved, Rejected, or PAID),
        // UPDATE it instead of creating a new one (due to OneToOne unique constraint)
        if (existingLoanRequest != null) {
            // Update the existing loan request with new values
            existingLoanRequest.setAmount(loanRequest.getAmount());
            existingLoanRequest.setPurpose(loanRequest.getPurpose());
            existingLoanRequest.setStatus("pending");
            existingLoanRequest.setCreatedAt(LocalDate.now()); // Update creation date
            savedLoanRequest = loanRequestRepo.save(existingLoanRequest);
        } else {
            // No existing loan request, create a new one
            loanRequest.setUsers(users);
            loanRequest.setStatus("pending");
            savedLoanRequest = loanRequestRepo.save(loanRequest);

            // Update the user's loanRequest reference
            users.setLoanRequest(savedLoanRequest);
            userRepo.save(users);
        }

        // Create a notice
        Notice notice = new Notice();
        notice.setType("Loan Request for Rs " + savedLoanRequest.getAmount());
        notice.setPurpose(savedLoanRequest.getPurpose() + "  Status : " + savedLoanRequest.getStatus());
        notice.setLoanid(savedLoanRequest.getLoanReqId());
        notice.setNoticeCreator(users.getName());

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
            Loan loan= new Loan();
            loan.setPrincipal(loanRequest.getAmount());
            loan.setUsers(loanRequest.getUsers());
            loanRepo.save(loan);
            noticeByPurpose.setPurpose(loanRequest.getPurpose() + "  Status : " + loanRequest.getStatus());
            noticeByPurpose.setLoanid(loan.getId());
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

    public List<LoanRequest> getAllLoanRequests() {
        List<LoanRequest> all = loanRequestRepo.findAll();
        return all;
    }

    public List<Loan> getAllActiveLoans() {
        return loanRepo.findAll().stream()
                .filter(loan -> "ACTIVE".equals(loan.getStatus()))
                .collect(java.util.stream.Collectors.toList());
    }

    public List<LoanRequest> getLoanHistory() {
        // Get all loan requests that are not pending (approved or rejected)
        return loanRequestRepo.findAll().stream()
                .filter(lr -> !"pending".equalsIgnoreCase(lr.getStatus()))
                .collect(java.util.stream.Collectors.toList());
    }

    @Transactional
    public Loan markLoanAsPaid(Long loanId, Long adminId, Long userID) {
        Users admin = userRepo.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found"));

        if (!"ADMIN".equalsIgnoreCase(admin.getRole())) {
            throw new RuntimeException("Only admins can mark loans as paid");
        }

        Loan loan = loanRepo.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));

        // Verify the loan belongs to the user
        if (!loan.getUsers().getUserID().equals(userID)) {
            throw new IllegalArgumentException("Loan does not belong to the specified user");
        }

        // Check if loan is already paid
        if ("PAID".equalsIgnoreCase(loan.getStatus())) {
            throw new IllegalStateException("Loan is already marked as paid");
        }

        // Use the OneToOne relationship to get the loan request
        Users loanUser = loan.getUsers();
        LoanRequest loanRequest = loanUser.getLoanRequest();

        if (loanRequest == null) {
            throw new IllegalStateException("No loan request found for this loan");
        }

        // Check if loan request is already paid
        if ("PAID".equalsIgnoreCase(loanRequest.getStatus())) {
            throw new IllegalStateException("Loan request is already marked as paid");
        }

        // Update loan status
        loan.setStatus("PAID");
        loan.setRemainingBalance(0.0);

        // Update loan request status
        loanRequest.setStatus("PAID");

        // Update notice
        String oldPurpose = loanRequest.getPurpose() + "  Status : " + loanRequest.getStatus();
        Notice noticeByPurpose = noticeRepo.getNoticeByPurpose(oldPurpose);
        if (noticeByPurpose != null) {
            noticeByPurpose.setPurpose(loanRequest.getPurpose() + "  Status : " + loanRequest.getStatus());
            noticeRepo.save(noticeByPurpose);
        }

        // Save both
        loanRequestRepo.save(loanRequest);
        return loanRepo.save(loan);
    }

    // Add this method for loan history (includes PAID loans)
    public List<Loan> getAllLoans() {
        return loanRepo.findAll();
    }
}