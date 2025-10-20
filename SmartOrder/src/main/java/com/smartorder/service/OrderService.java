package com.smartorder.service;

import com.smartorder.client.InventoryClient;
import com.smartorder.event.EventPublisher;
import com.smartorder.grpc.PaymentClient;
import com.smartorder.model.Order;
import com.smartorder.model.Status;
import com.smartorder.producer.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final PaymentClient paymentClient;
    private final InventoryClient inventoryClient;
    private final EventPublisher eventPublisher;
    @Autowired
    private KafkaProducer kafkaProducer;
    private final Map<Long, Order> db = new HashMap<>();

    public Order createOrder(Order order) {
        System.out.println("🚀 [OrderService] Starting createOrder() for product " + order.getProductId());
        System.out.println("🧩 [OrderService] KafkaProducer instance: " + kafkaProducer);

        try {
            order.setId((long) (db.size() + 1));
            order.setStatus(Status.CREATED);
            System.out.println("🆕 [OrderService] New order created with ID " + order.getId());

            boolean available = inventoryClient.checkStock(order.getProductId(), order.getQuantity());
            System.out.println("📦 [OrderService] Stock check result: " + available);

            if (!available) {
                order.setStatus(Status.FAILED);
                kafkaProducer.sendOrderError(order.getCustomerEmail(), order.getId().toString());
                db.put(order.getId(), order);
                System.out.println("❌ [OrderService] Stock unavailable. Error event sent.");
                return order;
            }

            boolean paid = paymentClient.processPayment(order.getId().toString(), order.getAmount());
            System.out.println("💰 [OrderService] Payment result: " + paid);

            if (paid) {
                order.setStatus(Status.PAID);
                eventPublisher.publishOrderPaid(order.getId());
                kafkaProducer.sendOrderConfirmation(order.getCustomerEmail(), order.getId().toString());
                System.out.println("✅ [OrderService] Payment successful. Confirmation event sent.");
            } else {
                order.setStatus(Status.FAILED);
                kafkaProducer.sendOrderError(order.getCustomerEmail(), order.getId().toString());
                System.out.println("❌ [OrderService] Payment failed. Error event sent.");
            }

            db.put(order.getId(), order);
            System.out.println("💾 [OrderService] Order saved to DB with status " + order.getStatus());
            return order;

        } catch (Exception e) {
            order.setStatus(Status.FAILED);
            kafkaProducer.sendOrderError(order.getCustomerEmail(), order.getId().toString());
            System.out.println("🔥 [OrderService] Exception occurred during order creation!");
            e.printStackTrace();
            return order;
        }
    }



    public List<Order> getAllOrder() {
        return db.values().stream().toList();
    }
}
