package com.luanpaiva.product_service.adapters.out.jpa;

import com.luanpaiva.product_service.adapters.out.jpa.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductsRepositoryJpa extends JpaRepository<ProductEntity, UUID> {

    @Query("FROM ProductEntity p WHERE p.id = :productId AND p.availableQuantity >= :quantity")
    Optional<ProductEntity> checkProductInventory(UUID productId, Integer quantity);

    @Modifying
    @Query("""
            UPDATE ProductEntity p
            SET p.availableQuantity = (p.availableQuantity - :quantity)
            WHERE p.id = :productId
            """)
    void updateInventory(UUID productId, Integer quantity);
}
