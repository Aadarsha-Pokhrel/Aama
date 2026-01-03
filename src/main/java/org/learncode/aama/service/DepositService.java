package org.learncode.aama.service;

import org.learncode.aama.Dao.DepositRepo;
import org.learncode.aama.Dao.UserRepo;
import org.learncode.aama.entites.Deposit;
import org.learncode.aama.entites.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepositService {
    @Autowired
    private DepositRepo depositRepo;
    @Autowired
    private UserRepo userRepo;

    public Deposit createDeposit(Long userId, Deposit deposit) {
        Optional<Users> user = userRepo.findById(userId);
        if (user.isPresent()) {
            Users users = user.get();
            deposit.setUsers(users);
            Deposit savedDeposit = depositRepo.save(deposit);
            return savedDeposit;
        }
        return null;
    }

    public List<Deposit> getDepositsByUserId(Long userId) {
        return depositRepo.findByUsers_UserID(userId);
    }

    public Deposit updateDepositStatus(Long depositId, String status) {
        Optional<Deposit> deposit = depositRepo.findById(depositId);
        if (deposit.isPresent()) {
            Deposit depositEntity = deposit.get();
            depositEntity.setStatus(status);
            return depositRepo.save(depositEntity);
        }
        return null;
    }

    public Deposit getDepositById(Long depositId) {
        Optional<Deposit> deposit = depositRepo.findById(depositId);
        return deposit.orElse(null);
    }

    public List<Deposit> getAllDeposits() {
        return depositRepo.findAll();
    }

    public void deleteDeposit(Long depositId) {
        depositRepo.deleteById(depositId);
    }
}

