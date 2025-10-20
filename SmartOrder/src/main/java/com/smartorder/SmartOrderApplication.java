package com.smartorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.smartorder"})
public class SmartOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartOrderApplication.class, args);
    }

}
