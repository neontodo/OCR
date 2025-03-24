package com.example.springserver.services;

import com.sendgrid.*;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

public class EmailService {
    
    //email key here

    private final Email sendFrom = new Email("negreaandrei90@gmail.com");
    private Email sendTo;

    public void sendVerificationEmail(String toEmail, String verificationLink) throws IOException {
        String subject = "Account Verification";
        this.sendTo = new Email(toEmail);
        Content content = new Content("text/plain", "Please click the following link to verify your account: " + verificationLink);
        Mail mail = new Mail(sendFrom, subject, sendTo, content);

        SendGrid sg = new SendGrid(SENDGRID_API_KEY);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
        } catch (IOException ex) {
            throw ex;
        }
    }
}
