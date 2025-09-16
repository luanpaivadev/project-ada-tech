package com.luanpaiva.order_service.core.ports.out;

import com.luanpaiva.order_service.adapters.out.model.ProductDTO;

import java.util.UUID;

public interface ProductServicePort {

    ProductDTO checkProductInventory(UUID productID, Integer quantity, String accessToken);
}
