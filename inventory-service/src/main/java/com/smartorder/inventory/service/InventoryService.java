package com.smartorder.inventory.service;

import com.smartorder.inventory.model.Product;
import com.smartorder.inventory.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class InventoryService {
    private final ProductRepository repository;

    public InventoryService(ProductRepository repository) {
        this.repository = repository;
    }
    public boolean isStock(String productId, int requestQuantity){
        Optional<Product> product = repository.findByProductId(productId);
        return product.map(p-> p.getStockQuantity()>=requestQuantity).orElse(false);
    }
    public void decreaseStock(String productId, int decQuantity){
        repository.findByProductId(productId).ifPresent(p->{
            p.setStockQuantity(p.getStockQuantity()-decQuantity);
            repository.save(p);
        });
    }
}
