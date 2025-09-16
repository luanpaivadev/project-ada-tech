package com.luanpaiva.product_service.core.ports.in;

import com.luanpaiva.product_service.adapters.in.listener.amqp.model.OrderDTO;
import com.luanpaiva.product_service.core.model.Product;

import java.util.UUID;

public interface ProductServicePort {

    Product checkProductInventory(UUID productId, Integer quantity);

    void removeProductsFromInventory(OrderDTO order);
}
