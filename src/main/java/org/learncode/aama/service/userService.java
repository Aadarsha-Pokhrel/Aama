package org.learncode.aama.service;

import org.learncode.aama.Dao.UserRepo;
import org.learncode.aama.entites.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class userService {
    @Autowired
    private UserRepo userDao;

    public Users saveUser(Users users){
        Users save = userDao.save(users);
        return save;
    }


}
