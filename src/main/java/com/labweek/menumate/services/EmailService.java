package com.labweek.menumate.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendOtpEmail(String toEmail, int otp) {
        // Create a simple email message
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Registration OTP Confirmation");
        message.setText("Thanks for registering with Comcast Buy & Sell\n" +
                "Please use this OTP to confirm your registration: " + otp);

        // Send the email
        mailSender.send(message);
    }
}