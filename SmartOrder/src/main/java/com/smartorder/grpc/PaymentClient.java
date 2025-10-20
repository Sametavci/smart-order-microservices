package com.smartorder.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Component;

@Component

public class PaymentClient {
    private final ManagedChannel channel;
    private final PaymentServiceGrpc.PaymentServiceBlockingStub stub;
    public PaymentClient() {
        this.channel = ManagedChannelBuilder
                .forAddress("payment-service", 50051)
                .usePlaintext()
                .build();
        this.stub = PaymentServiceGrpc.newBlockingStub(channel);
    }

    public boolean processPayment(String orderId, double amount) {
        PaymentRequest request = PaymentRequest.newBuilder()
                .setOrderId(orderId)
                .setAmount(amount)
                .build();

        PaymentResponse response = stub.processPayment(request);
        System.out.println("Payment Service Response: " + response.getMessage());
        return response.getSuccess();
    }
}
