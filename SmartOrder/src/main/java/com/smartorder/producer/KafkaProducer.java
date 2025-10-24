package com.smartorder.producer;

import com.smartorder.model.NotificationEvent;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.errors.TopicExistsException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC_NAME = "notification-events";
    private static final String BOOTSTRAP_SERVERS = "boot-bfgwhvlm.c1.kafka-serverless.eu-north-1.amazonaws.com:9098";

    public KafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    private void sendWithTopicCheck(Object event, String orderId, String eventType) {
        try {
            System.out.println(" [KafkaProducer] Sending event to Kafka topic '" + TOPIC_NAME + "': " + event);
            kafkaTemplate.send(TOPIC_NAME, event);
            System.out.println(" [KafkaProducer] Successfully sent " + eventType + " event for order " + orderId);
        } catch (Exception ex) {
            if (ex.getMessage() != null && ex.getMessage().contains("UNKNOWN_TOPIC_OR_PARTITION")) {
                System.out.println("[KafkaProducer] Topic not found, attempting to create: " + TOPIC_NAME);
                createTopicIfMissing(TOPIC_NAME);
                try {
                    Thread.sleep(2000); // topic oluşması için kısa bekleme
                    kafkaTemplate.send(TOPIC_NAME, event);
                    System.out.println("[KafkaProducer] Retried and sent event for order " + orderId);
                } catch (Exception retryEx) {
                    System.out.println(" [KafkaProducer] Retry failed: " + retryEx.getMessage());
                }
            } else {
                System.out.println(" [KafkaProducer] Failed to send event for order " + orderId);
                ex.printStackTrace();
            }
        }
    }

    private void createTopicIfMissing(String topicName) {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        configs.put("security.protocol", "SASL_SSL");
        configs.put("sasl.mechanism", "AWS_MSK_IAM");
        configs.put("sasl.jaas.config", "software.amazon.msk.auth.iam.IAMLoginModule required;");
        configs.put("sasl.client.callback.handler.class", "software.amazon.msk.auth.iam.IAMClientCallbackHandler");

        try (AdminClient admin = AdminClient.create(configs)) {
            NewTopic topic = new NewTopic(topicName, 3, (short) 1);
            admin.createTopics(Collections.singleton(topic)).all().get();
            System.out.println("🟢 [KafkaProducer] Created missing topic: " + topicName);
        } catch (ExecutionException e) {
            if (e.getCause() instanceof TopicExistsException) {
                System.out.println(" [KafkaProducer] Topic already exists: " + topicName);
            } else {
                System.out.println("[KafkaProducer] Topic creation failed: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println(" [KafkaProducer] Error while creating topic: " + e.getMessage());
        }
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

        sendWithTopicCheck(event, orderId, "confirmation");
    }

    public void sendOrderError(String email, String orderId) {
        System.out.println("🟡 [KafkaProducer] Preparing to send error event for order " + orderId);

        NotificationEvent event = new NotificationEvent(
                email,
                "Order Failed",
                "Your order #" + orderId + " has failed!"
        );

        sendWithTopicCheck(event, orderId, "error");
    }
}
