package org.learncode.aama.service;

import org.learncode.aama.Dao.UserRepo;
import org.learncode.aama.entites.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class userService {
    @Autowired
    private UserRepo userDao;
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    public Users saveUser(Users users){
        users.setPassword(bCryptPasswordEncoder.encode(users.getPassword()));
        Users save = userDao.save(users);
        return save;
    }



}
