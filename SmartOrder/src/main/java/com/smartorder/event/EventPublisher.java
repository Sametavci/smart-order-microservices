package com.smartorder.event;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventPublisher {
    private final KafkaTemplate<String,String> kafkaTemplate;

    EventPublisher(KafkaTemplate<String,String> kafkaTemplate){
        this.kafkaTemplate=kafkaTemplate;
    }
    public void publishOrderPaid(Long orderId){
        String msg = "Order "+ orderId + "has been paid!";
        kafkaTemplate.send("order-paid",msg);
        System.out.println("📤 Kafka event published: " + msg);

    }
}
