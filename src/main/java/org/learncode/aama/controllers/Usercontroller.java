package org.learncode.aama.controllers;

import jakarta.servlet.http.HttpSession;
import org.learncode.aama.Dao.LoanRepo;
import org.learncode.aama.Dao.UserRepo;
import org.learncode.aama.Dto.userDto;
import org.learncode.aama.entites.Loan;
import org.learncode.aama.entites.UserPrincipal;
import org.learncode.aama.entites.Users;
import org.learncode.aama.service.jwtService;
import org.learncode.aama.service.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
public class Usercontroller {
    @Autowired
    private userService UserService;
    @Autowired
    private LoanRepo loanRepo;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private jwtService jwtService;
    @Autowired
    private UserRepo userRepo;

    @PostMapping("/register")
    public Users registerUser(@RequestBody Users users){
        Users users1 = UserService.saveUser(users);
        return users1;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody userDto loginDto) {
        try {
            // Get user by name
            Users user = userRepo.findUsersByName(loginDto.getName());

            if (user == null) {
                return ResponseEntity.status(401).body("User not found");
            }

            // Authenticate using userID and password
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUserID().toString(),
                            loginDto.getPassword()
                    )
            );

            if (authentication.isAuthenticated()) {
                // Generate JWT token
                String token = jwtService.getToken(user);

                // Return token
                HashMap<String, String> response = new HashMap<>();
                response.put("token", token);
                response.put("message", "Login successful");

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(401).body("Login Failed");
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Login Failed: " + e.getMessage());
        }
    }
    @GetMapping("/dashboard")
    public Users dashboard(Model model) {
        // Get authenticated user from JWT
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
        Users user = principal.getUser();

        // Get active loan for this user
        Loan loan = loanRepo.findLoansByUsers_UserID(user.getUserID());
        if (loan != null && "ACTIVE".equals(loan.getStatus())) {
            model.addAttribute("loan", loan);
        }

        return user;
    }

}




