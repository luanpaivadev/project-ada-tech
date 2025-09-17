package com.luanpaiva.notification_service.adapters.in.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luanpaiva.notification_service.adapters.in.listener.model.OrderDTO;
import com.luanpaiva.notification_service.core.ports.out.NotificationServicePort;
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
    private final NotificationServicePort notificationServicePort;

    @RabbitHandler
    public void statusOrderNotification(byte[] message) throws IOException {
        OrderDTO orderDTO = mapper.readValue(message, OrderDTO.class);
        notificationServicePort.notify(orderDTO);
    }
}
