package com.luanpaiva.order_service.core.ports.out;

import com.luanpaiva.order_service.core.model.Order;
import com.luanpaiva.order_service.core.model.StatusOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepositoryPort {

    Optional<Order> findById(UUID orderId);

    Order createOrUpdateOrder(Order order);

    void updateStatusOrder(UUID orderId, StatusOrder statusOrder);

    Page<Order> findOrdersInSeparation(Pageable pageable);
}
