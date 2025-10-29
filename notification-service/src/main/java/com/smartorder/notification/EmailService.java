package com.smartorder.notification;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final String fromAddress;

    public EmailService(JavaMailSender javaMailSender,
                        @Value("${spring.mail.from:no-reply@smartorder.com}") String fromAddress) {
        this.javaMailSender = javaMailSender;
        this.fromAddress = fromAddress;
        System.out.println("📬 Spring mail from address (injected): " + fromAddress);
    }

    public void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();

            msg.setFrom(fromAddress);
            msg.setTo(to);
            msg.setSubject(subject);
            msg.setText(text);

            javaMailSender.send(msg);

            System.out.println(" Mail sent successfully from " + fromAddress + " to " + to);
        } catch (Exception e) {
            System.err.println(" Mail sending failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
