package com.smartorder.inventory.repository;

import com.smartorder.inventory.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {
    Optional<Product> findByProductId(String productId);

}
