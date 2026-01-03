package org.learncode.aama.service;

import org.learncode.aama.Dao.UserRepo;
import org.learncode.aama.entites.UserPrincipal;
import org.learncode.aama.entites.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // username is actually userID (as string) from JWT token
        try {
            Long userId = Long.parseLong(username);
            Optional<Users> userOptional = userRepo.findById(userId);

            if (userOptional.isEmpty()) {
                throw new UsernameNotFoundException("User not found with ID: " + username);
            }

            Users user = userOptional.get();
            return new UserPrincipal(user);
        } catch (NumberFormatException e) {
            throw new UsernameNotFoundException("Invalid user ID format: " + username);
        }
    }
}