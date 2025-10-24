package com.smartorder.grpc;

import com.smartorder.payment.grpc.PaymentRequest;
import com.smartorder.payment.grpc.PaymentResponse;
import com.smartorder.payment.grpc.PaymentServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PaymentClient {

    private final ManagedChannel channel;
    private final PaymentServiceGrpc.PaymentServiceBlockingStub stub;

    public PaymentClient(
            @Value("${payment.host}") String host,
            @Value("${payment.port}") int port) {

        System.out.println("🔗 [PaymentClient] Connecting to gRPC server at " + host + ":" + port);

        this.channel = ManagedChannelBuilder
                .forAddress(host, port)
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

        System.out.println("💳 [PaymentClient] Payment Service Response: " + response.getMessage());
        return response.getSuccess();
    }
}
