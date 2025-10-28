package com.smartorder.event;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public EventPublisher(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishOrderPaid(Long orderId) {
        String msg = "Order " + orderId + " has been paid!";

        try {
            kafkaTemplate.send("order-paid", msg).get();
            System.out.println(" [EventPublisher] Kafka event published to 'order-paid': " + msg);
        } catch (Exception e) {
            System.err.println(" [EventPublisher] Failed to send Kafka message: " + e.getMessage());
        }
    }
}
