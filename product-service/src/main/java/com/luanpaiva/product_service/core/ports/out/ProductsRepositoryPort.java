package com.luanpaiva.product_service.core.ports.out;

import com.luanpaiva.product_service.core.model.Product;

import java.util.Optional;
import java.util.UUID;

public interface ProductsRepositoryPort {

    Optional<Product> checkProductInventory(UUID productId, Integer quantity);

    void updateInventory(UUID productId, Integer quantity);
}
