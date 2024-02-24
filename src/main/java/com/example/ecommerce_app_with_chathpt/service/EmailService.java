package com.example.ecommerce_app_with_chathpt.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmailService  {


    private JavaMailSender javaMailSender;

    @Async
    public void sendEmail(SimpleMailMessage mailMessage) {
        javaMailSender.send(mailMessage);
    }
}
