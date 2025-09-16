package com.luanpaiva.order_service.core.ports.in;

import com.luanpaiva.order_service.adapters.in.model.OrderInput;
import com.luanpaiva.order_service.adapters.in.model.PaymentWebhookPayload;
import com.luanpaiva.order_service.adapters.out.model.OrderDTO;
import com.luanpaiva.order_service.core.model.Order;
import com.luanpaiva.order_service.core.model.StatusOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderServicePort {

    Order findById(UUID orderId);

    Order findById(UUID orderId, String customerEmail);

    Order createNewOrder(OrderInput orderInput, String customerEmail);

    void validatePayment(PaymentWebhookPayload payload);

    void updateStatusOrder(UUID orderId, StatusOrder statusOrder);

    Page<OrderDTO> findOrdersInSeparation(Pageable pageable);
}
