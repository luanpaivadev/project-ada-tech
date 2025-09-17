package com.luanpaiva.order_service.adapters.out.repository.jpa;

import com.luanpaiva.order_service.adapters.out.repository.model.OrderEntity;
import com.luanpaiva.order_service.core.model.StatusOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepositoryJpa extends JpaRepository<OrderEntity, UUID> {

    @Modifying
    @Query("UPDATE OrderEntity o SET o.status = :statusOrder WHERE o.id = :orderId")
    Optional<OrderEntity> updateStatusOrder(UUID orderId, StatusOrder statusOrder);

    @Query("FROM OrderEntity o WHERE o.status = 'IN_SEPARATION'")
    Page<OrderEntity> findOrdersInSeparation(Pageable pageable);
}
