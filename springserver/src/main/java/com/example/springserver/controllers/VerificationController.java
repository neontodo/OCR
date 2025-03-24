package com.example.springserver.controllers;

import com.example.springserver.models.User;
import com.example.springserver.repositories.UserRepository;
import com.example.springserver.services.VerificationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@AllArgsConstructor
@RequestMapping("/verify")
public class VerificationController {
    private final VerificationService verificationService;
    private final UserRepository userRepository;

    @GetMapping
    public String verifyAccount(@RequestParam("token") String token) {
        boolean isVerified = verificationService.verifyAccount(token);

        if (isVerified) {
            // Handle successful verification
            User user = userRepository.findUserByVerificationToken(token);
            if(user.getVerified()){
                return "already-verified";  //Redirect to an already verified page
            } else {
                user.setVerified(true);
                userRepository.save(user);
                return "verified";  // Redirect to a success page
            }
        } else {
            // Handle failed verification
            return "verification-failed"; // Redirect to a failure page
        }
    }
}
