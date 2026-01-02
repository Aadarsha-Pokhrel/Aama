package org.learncode.aama.Dao;

import org.learncode.aama.entites.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepo extends JpaRepository<Loan, Long> {
    Loan findLoansByUsers_UserID(Long usersUserID);

    Loan findLoansByStatus(String status);
}
