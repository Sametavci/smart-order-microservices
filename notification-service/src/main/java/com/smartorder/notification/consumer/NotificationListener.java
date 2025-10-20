package com.smartorder.notification.consumer;

import com.smartorder.model.NotificationEvent;
import com.smartorder.notification.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationListener {

    private final EmailService emailService;

    public NotificationListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "notification-events", groupId = "notification-group")
    public void listen(NotificationEvent event) {
        System.out.println("Received Kafka Event: " + event);

        if (event.getEmail() != null && event.getSubject() != null && event.getMessage() != null) {
            emailService.sendEmail(event.getEmail(), event.getSubject(), event.getMessage());
            System.out.println("Mail sent successfully to " + event.getEmail());
        } else {
            System.out.println("Missing fields in Kafka message: " + event);
        }
    }

}
