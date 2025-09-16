package com.luanpaiva.product_service.core.services;

import com.luanpaiva.product_service.adapters.in.listener.amqp.model.OrderDTO;
import com.luanpaiva.product_service.core.exceptions.NotFoundException;
import com.luanpaiva.product_service.core.model.Product;
import com.luanpaiva.product_service.core.ports.in.ProductServicePort;
import com.luanpaiva.product_service.core.ports.out.ProductsRepositoryPort;

import java.util.UUID;

public class ProductService implements ProductServicePort {

    private final ProductsRepositoryPort productsRepositoryPort;

    public ProductService(ProductsRepositoryPort productsRepositoryPort) {
        this.productsRepositoryPort = productsRepositoryPort;
    }

    @Override
    public Product checkProductInventory(UUID productId, Integer quantity) {
        return productsRepositoryPort.checkProductInventory(productId, quantity)
                .orElseThrow(() -> new NotFoundException("Product out of stock or unavailable."));
    }

    @Override
    public void removeProductsFromInventory(OrderDTO order) {
        order.getItems().forEach(item -> {
            UUID productId = item.getProduct().getProductId();
            Integer quantity = item.getQuantity();
            productsRepositoryPort.updateInventory(productId, quantity);
        });
    }
}
