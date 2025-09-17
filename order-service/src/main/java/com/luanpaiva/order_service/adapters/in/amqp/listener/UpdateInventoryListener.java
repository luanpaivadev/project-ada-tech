package com.luanpaiva.order_service.adapters.in.amqp.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luanpaiva.order_service.adapters.out.model.OrderDTO;
import com.luanpaiva.order_service.core.model.Order;
import com.luanpaiva.order_service.core.model.StatusOrder;
import com.luanpaiva.order_service.core.ports.in.OrderServicePort;
import com.luanpaiva.order_service.core.ports.out.SendMessagePort;
import com.luanpaiva.order_service.core.utils.Queues;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
@RabbitListener(queues = "order.inventory_successfully_updated")
public class UpdateInventoryListener {

    private final OrderServicePort orderServicePort;
    private final ObjectMapper mapper;
    private final SendMessagePort sendMessagePort;

    @RabbitHandler
    public void orderUpdateInventory(byte[] message) throws IOException {
        OrderDTO orderDTO = mapper.readValue(message, OrderDTO.class);
        log.info("Atualizando status do pedido para {}", StatusOrder.IN_SEPARATION);
        Order order = orderServicePort.updateStatusOrder(orderDTO.getId(), StatusOrder.IN_SEPARATION);
        log.info("Status atualizado com sucesso.");
        sendMessagePort.send(Queues.NOTIFICATION_STATUS_ORDER_QUEUE, order, OrderDTO.class);
    }
}
