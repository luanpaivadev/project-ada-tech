package com.luanpaiva.product_service.core.ports.in;

import com.luanpaiva.product_service.adapters.in.listener.amqp.model.OrderDTO;
import com.luanpaiva.product_service.core.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductServicePort {

    Product checkProductInventory(UUID productId, Integer quantity);

    void removeProductsFromInventory(OrderDTO order);

    Page<Product> findAll(Pageable pageable);
}
