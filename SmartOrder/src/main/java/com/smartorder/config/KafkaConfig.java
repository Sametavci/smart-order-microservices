package com.smartorder.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        config.put("security.protocol", "SASL_SSL");
        config.put("sasl.mechanism", "AWS_MSK_IAM");
        config.put("sasl.jaas.config", "software.amazon.msk.auth.iam.IAMLoginModule required;");
        config.put("sasl.client.callback.handler.class", "software.amazon.msk.auth.iam.IAMClientCallbackHandler");

        System.out.println("🧩 [KafkaConfig] ProducerFactory initialized with AWS MSK IAM and JsonSerializer, bootstrap: " + bootstrapServers);
        return new DefaultKafkaProducerFactory<>(config);
    }


    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        System.out.println("⚙️ [KafkaConfig] KafkaTemplate bean created");
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ProducerFactory<String, String> stringProducerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put("security.protocol", "SASL_SSL");
        config.put("sasl.mechanism", "AWS_MSK_IAM");
        config.put("sasl.jaas.config", "software.amazon.msk.auth.iam.IAMLoginModule required;");
        config.put("sasl.client.callback.handler.class", "software.amazon.msk.auth.iam.IAMClientCallbackHandler");
        System.out.println("🧩 [KafkaConfig] String ProducerFactory initialized, bootstrap: " + bootstrapServers);
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, String> stringKafkaTemplate() {
        System.out.println("⚙️ [KafkaConfig] String KafkaTemplate bean created");
        return new KafkaTemplate<>(stringProducerFactory());
    }

}
