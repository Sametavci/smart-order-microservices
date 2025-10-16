package com.smartorder.inventory.controller;

import com.smartorder.inventory.grpc.InventoryServiceGrpc;
import com.smartorder.inventory.grpc.StockRequest;
import com.smartorder.inventory.grpc.StockResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @GetMapping("/check")
    public String checkStock(@RequestParam String productId, @RequestParam int quantity) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("inventory-service", 50052)
                .usePlaintext()
                .build();

        InventoryServiceGrpc.InventoryServiceBlockingStub stub =
                InventoryServiceGrpc.newBlockingStub(channel);

        StockRequest request = StockRequest.newBuilder()
                .setProductId(productId)
                .setQuantity(quantity)
                .build();

        StockResponse response = stub.checkStock(request);
        channel.shutdown();
        return response.getMessage();
    }
}
