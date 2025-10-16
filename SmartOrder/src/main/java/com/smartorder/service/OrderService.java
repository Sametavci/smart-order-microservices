package com.smartorder.service;

import com.smartorder.client.InventoryClient;
import com.smartorder.grpc.PaymentClient;
import com.smartorder.model.Order;
import com.smartorder.model.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final PaymentClient paymentClient;
    private final InventoryClient inventoryClient;

    private final Map<Long, Order> db = new HashMap<>();

    public Order createOrder(Order order) {
        order.setId((long) (db.size() + 1));
        order.setStatus(Status.CREATED);

        boolean available = inventoryClient.checkStock(order.getProductId(), order.getQuantity());
        if (!available) {
            order.setStatus(Status.FAILED);
            db.put(order.getId(), order);
            return order;
        }

        boolean paid = paymentClient.processPayment(order.getId().toString(), order.getAmount());
        if (paid) order.setStatus(Status.PAID);
        else order.setStatus(Status.FAILED);

        db.put(order.getId(), order);
        return order;
    }

    public List<Order> getAllOrder() {
        return db.values().stream().toList();
    }
}
