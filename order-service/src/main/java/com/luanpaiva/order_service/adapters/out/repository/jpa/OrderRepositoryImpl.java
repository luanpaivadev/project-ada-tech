package com.luanpaiva.order_service.adapters.out.repository.jpa;

import com.luanpaiva.order_service.adapters.out.model.OrderDTO;
import com.luanpaiva.order_service.adapters.out.repository.model.OrderEntity;
import com.luanpaiva.order_service.core.model.Order;
import com.luanpaiva.order_service.core.model.StatusOrder;
import com.luanpaiva.order_service.core.ports.out.OrderRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryPort {

    private final OrderRepositoryJpa repositoryJpa;
    private final ModelMapper mapper;

    @Override
    public Optional<Order> findById(UUID orderId) {
        Optional<OrderEntity> optional = repositoryJpa.findById(orderId);
        return optional.map(orderEntity -> mapper.map(orderEntity, Order.class));
    }

    @Override
    @Transactional
    public Order createOrUpdateOrder(Order order) {
        OrderEntity orderEntity = mapper.map(order, OrderEntity.class);
        orderEntity = repositoryJpa.save(orderEntity);
        return mapper.map(orderEntity, Order.class);
    }

    @Override
    @Transactional
    public Optional<Order> updateStatusOrder(UUID orderId, StatusOrder statusOrder) {
        return repositoryJpa.updateStatusOrder(orderId, statusOrder)
                .map(orderEntity -> mapper.map(orderEntity, Order.class));
    }

    @Override
    public Page<Order> findOrdersInSeparation(Pageable pageable) {
        return repositoryJpa.findOrdersInSeparation(pageable)
                .map(orderEntity -> mapper.map(orderEntity, Order.class));
    }
}
