package com.luanpaiva.product_service.adapters.out.jpa;

import com.luanpaiva.product_service.core.model.Product;
import com.luanpaiva.product_service.core.ports.out.ProductsRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProductsRepositoryImpl implements ProductsRepositoryPort {

    private final ProductsRepositoryJpa repositoryJpa;
    private final ModelMapper mapper;

    @Override
    public Optional<Product> checkProductInventory(UUID productId, Integer quantity) {
        return repositoryJpa.checkProductInventory(productId, quantity)
                .map(productEntity -> mapper.map(productEntity, Product.class));
    }

    @Override
    @Transactional
    public void updateInventory(UUID productId, Integer quantity) {
        repositoryJpa.updateInventory(productId, quantity);
    }
}
