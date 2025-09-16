package com.luanpaiva.notification_service.adapters.in.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luanpaiva.notification_service.adapters.in.listener.model.StatusOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
@RabbitListener(queues = "notification.status_order")
public class StatusOrderListener {

    private final ObjectMapper mapper;

    @RabbitHandler
    public void statusOrderNotification(byte[] message) throws IOException {
        StatusOrder statusOrder = mapper.readValue(message, StatusOrder.class);
        log.info("Status Order: {}", statusOrder);
        // TODO - DE ACORDO COM O STATUS DO PEDIDO FOR AVANÇANDO, EXIBIR NA MENSAGEM DO E-MAIL A EVOLUÇÃO DO PEDIDO.
    }
}
