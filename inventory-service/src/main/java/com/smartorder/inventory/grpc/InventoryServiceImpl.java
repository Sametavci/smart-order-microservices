package com.smartorder.inventory.grpc;

import com.smartorder.inventory.model.Product;
import com.smartorder.inventory.repository.ProductRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class InventoryServiceImpl extends InventoryServiceGrpc.InventoryServiceImplBase {

    private final ProductRepository productRepository;

    @Override
    public void checkStock(StockRequest request, StreamObserver<StockResponse> responseObserver) {
        System.out.println("Checking stock for product: " + request.getProductId() + " quantity: " + request.getQuantity());

        var productOpt = productRepository.findByProductId(request.getProductId());

        if (productOpt.isEmpty()) {
            StockResponse response = StockResponse.newBuilder()
                    .setAvailable(false)
                    .setMessage("Product not found: " + request.getProductId())
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

        Product product = productOpt.get();
        boolean available = product.getStockQuantity() >= request.getQuantity();
        if(available)
            product.setStockQuantity(product.getStockQuantity()- request.getQuantity());
        productRepository.save(product);

        StockResponse response = StockResponse.newBuilder()
                .setAvailable(available)
                .setMessage(available
                        ? " Stock available for product " + request.getProductId()
                        : " Not enough stock for product " + request.getProductId())
                .setMessage(String.valueOf((product.getStockQuantity()- request.getQuantity())))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
