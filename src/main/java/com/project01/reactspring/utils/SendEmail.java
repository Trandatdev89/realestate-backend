package com.project01.reactspring.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class SendEmail {

    @Value("{spring.mail.username}")
    private String fromEmailId;

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String content) {
         SimpleMailMessage message = new SimpleMailMessage();
         message.setFrom(fromEmailId);
         message.setTo(to);
         message.setSubject(subject);
         message.setText(content);
         mailSender.send(message);
    }

}
