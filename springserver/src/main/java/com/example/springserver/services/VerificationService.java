package com.example.springserver.services;

import com.example.springserver.models.User;
import com.example.springserver.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
@AllArgsConstructor
public class VerificationService {
    private final UserRepository userRepository;

    public void sendVerificationEmail(User user) {
        // Generate a unique verification token (you can use UUID or any secure method)
        String verificationToken = generateVerificationToken();

        // Save the verification token to the user's record in the database
        user.setVerificationToken(verificationToken);
        userRepository.save(user);

        // Build the verification link
        String verificationLink = buildVerificationLink(verificationToken);

        // Send the verification link to the user's email
        sendEmail(user.getEmailAddress(), verificationLink);
    }

    private String generateVerificationToken() {
        return UUID.randomUUID().toString();
    }

    private String buildVerificationLink(String verificationToken) {
        String domain = "http://localhost:8000";
        return domain + "/verify?token=" + verificationToken;
    }

    private void sendEmail(String sendTo, String verificationLink) {
        EmailService emailService = new EmailService();
        try {
            emailService.sendVerificationEmail(sendTo, verificationLink);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean verifyAccount(String token) {
        // Verify the user's account based on the token
        // Return true if verification is successful, false otherwise
        return true;
    }
}
