package com.smartorder.client;

import com.smartorder.inventory.grpc.InventoryServiceGrpc;
import com.smartorder.inventory.grpc.StockRequest;
import com.smartorder.inventory.grpc.StockResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class InventoryClient {

    private final InventoryServiceGrpc.InventoryServiceBlockingStub stub;

    public InventoryClient(
            @Value("${inventory.host}") String host,
            @Value("${inventory.port}") int port) {

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();

        this.stub = InventoryServiceGrpc.newBlockingStub(channel);

        System.out.println("✅ [InventoryClient] gRPC channel initialized to " + host + ":" + port);
    }

    public boolean checkStock(String productId, int quantity) {
        StockRequest request = StockRequest.newBuilder()
                .setProductId(productId)
                .setQuantity(quantity)
                .build();

        StockResponse response = stub.checkStock(request);
        System.out.println("📦 Inventory Service Response: " + response.getMessage());
        return response.getAvailable();
    }
}
