package org.learncode.aama.Dao;


import org.learncode.aama.entites.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepositRepo extends JpaRepository<Deposit, Long> {
    List<Deposit> findByUsers_UserID(Long userId);

    @Query("SELECT COALESCE(SUM(d.amount), 0) FROM Deposit d WHERE d.users.userID = :userId")
    Double sumDepositByUsers(@Param("userId") Long userId);

    List<Deposit> getDepositsByUsersUserID(Long usersUserID);
}

