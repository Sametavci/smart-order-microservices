package com.smartorder.producer;

import com.smartorder.model.NotificationEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrderConfirmation(String email, String orderId) {
        System.out.println("🟡 [KafkaProducer] Preparing to send confirmation event for order " + orderId);

        if (email == null || email.isEmpty()) {
            System.out.println("⚠️ [KafkaProducer] Email is null or empty for order " + orderId);
        }

        NotificationEvent event = new NotificationEvent(
                email,
                "Order Confirmed",
                "Your order #" + orderId + " has been confirmed!"
        );

        try {
            System.out.println("🟢 [KafkaProducer] Sending event to Kafka topic 'notification-events': " + event);
            kafkaTemplate.send("notification-events", event);
            System.out.println("✅ [KafkaProducer] Successfully sent confirmation event for order " + orderId);
        } catch (Exception e) {
            System.out.println("❌ [KafkaProducer] Failed to send event for order " + orderId);
            e.printStackTrace();
        }
    }

    public void sendOrderError(String email, String orderId) {
        System.out.println("🟡 [KafkaProducer] Preparing to send error event for order " + orderId);

        NotificationEvent event = new NotificationEvent(
                email,
                "Order Failed",
                "Your order #" + orderId + " has failed!"
        );

        try {
            System.out.println("🟢 [KafkaProducer] Sending event to Kafka topic 'notification-events': " + event);
            kafkaTemplate.send("notification-events", event);
            System.out.println("✅ [KafkaProducer] Successfully sent error event for order " + orderId);
        } catch (Exception e) {
            System.out.println("❌ [KafkaProducer] Failed to send error event for order " + orderId);
            e.printStackTrace();
        }
    }
}
