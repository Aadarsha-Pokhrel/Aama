package org.learncode.aama.controllers;

import org.learncode.aama.entites.Users;
import org.learncode.aama.service.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Usercontroller {
    @Autowired
    private userService UserService;

    @PostMapping("/register")
    public Users registerUser(@RequestBody Users users){
        Users users1 = UserService.saveUser(users);
        return users1;
    }


}
