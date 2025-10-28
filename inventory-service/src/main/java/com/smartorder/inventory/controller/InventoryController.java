package com.smartorder.inventory.controller;

import com.smartorder.inventory.grpc.InventoryServiceGrpc;
import com.smartorder.inventory.grpc.StockRequest;
import com.smartorder.inventory.grpc.StockResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final String host;
    private final int port;

    public InventoryController(
            @Value("${inventory.grpc.host}") String host,
            @Value("${inventory.grpc.port}") int port) {
        this.host = host;
        this.port = port;
    }

    @GetMapping("/check")
    public String checkStock(@RequestParam String productId, @RequestParam int quantity) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(host, port)
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
