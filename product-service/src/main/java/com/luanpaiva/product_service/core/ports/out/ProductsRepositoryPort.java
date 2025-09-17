package com.luanpaiva.product_service.core.ports.out;

import com.luanpaiva.product_service.core.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ProductsRepositoryPort {

    Optional<Product> checkProductInventory(UUID productId, Integer quantity);

    void updateInventory(UUID productId, Integer quantity);

    Page<Product> findAll(Pageable pageable);
}
