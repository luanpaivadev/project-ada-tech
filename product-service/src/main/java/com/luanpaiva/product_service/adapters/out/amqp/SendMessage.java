package com.luanpaiva.product_service.adapters.out.amqp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luanpaiva.product_service.core.exceptions.InternalServerErrorException;
import com.luanpaiva.product_service.core.ports.out.SendMessagePort;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

import static java.util.Objects.nonNull;

@Component
@RequiredArgsConstructor
public class SendMessage implements SendMessagePort {

    private final RabbitTemplate rabbitTemplate;
    private final ModelMapper modelMapper;
    private final ObjectMapper mapper;

    @Override
    public <T> void send(String routingKey, Object object, Class<T> clazz) {
        try {
            if (nonNull(clazz)) {
                object = modelMapper.map(object, clazz);
            }
            String json = mapper.writeValueAsString(object);
            Message message = MessageBuilder
                    .withBody(json.getBytes(StandardCharsets.UTF_8))
                    .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                    .build();
            rabbitTemplate.send(routingKey, message);
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
