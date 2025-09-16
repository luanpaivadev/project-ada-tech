package com.luanpaiva.product_service.adapters.in.listener.amqp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luanpaiva.product_service.adapters.in.listener.amqp.model.OrderDTO;
import com.luanpaiva.product_service.core.ports.in.ProductServicePort;
import com.luanpaiva.product_service.core.ports.out.SendMessagePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
@RabbitListener(queues = "product.payment_approved")
public class PaymentApprovedListener {

    private final ObjectMapper mapper;
    private final ProductServicePort productServicePort;
    private final SendMessagePort sendMessagePort;

    @RabbitHandler
    public void receive(byte[] message) throws IOException {
        OrderDTO order = mapper.readValue(message, OrderDTO.class);
        productServicePort.removeProductsFromInventory(order);
        sendMessagePort.send("order.inventory_successfully_updated", order, null);
    }
}
