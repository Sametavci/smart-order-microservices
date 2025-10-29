package com.smartorder.notification;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }
    public void sendEmail(String to, String subject, String text){
        try{
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(System.getenv("SPRING_MAIL_FROM"));
            msg.setTo(to);
            msg.setText(text);
            msg.setSubject(subject);
            javaMailSender.send(msg);
            System.out.println("Mail sent successfully to " + to);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
