package com.smartorder.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NotificationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
        System.out.println("🚀 NotificationService started and listening Kafka...");

        // Environment değişkenlerini kontrol et
        printEnv("AWS_SES_SMTP_HOST");
        printEnv("AWS_SES_SMTP_USERNAME");
        printEnvMasked("AWS_SES_SMTP_PASSWORD");
    }

    private static void printEnv(String key) {
        String value = System.getenv(key);
        if (value != null)
            System.out.println(" " + key + " = " + value);
        else
            System.out.println(" " + key + " NOT FOUND in environment!");
    }

    private static void printEnvMasked(String key) {
        String value = System.getenv(key);
        if (value != null && !value.isEmpty()) {
            String masked = value.length() > 6
                    ? value.substring(0, 4) + "..." + value.substring(value.length() - 3)
                    : "***";
            System.out.println(" " + key + " = " + masked + " (masked)");
        } else {
            System.out.println(" " + key + " NOT FOUND in environment!");
        }
    }
}
