package org.learncode.aama.Dao;


import org.learncode.aama.entites.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepositRepo extends JpaRepository<Deposit,Long> {
    List<Deposit> findByUsers_UserID(Long usersUsesID);

}
