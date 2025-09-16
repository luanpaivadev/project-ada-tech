package com.luanpaiva.order_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.luanpaiva.order_service.core.ports.in.OrderServicePort;
import com.luanpaiva.order_service.core.ports.out.AuthServicePort;
import com.luanpaiva.order_service.core.ports.out.CacheServicePort;
import com.luanpaiva.order_service.core.ports.out.OrderRepositoryPort;
import com.luanpaiva.order_service.core.ports.out.ProductServicePort;
import com.luanpaiva.order_service.core.ports.out.SendMessagePort;
import com.luanpaiva.order_service.core.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public OrderServicePort orderServicePort(OrderRepositoryPort orderRepositoryPort,
                                             ProductServicePort productServicePort,
                                             SendMessagePort sendMessagePort,
                                             AuthServicePort authServicePort,
                                             CacheServicePort<String, Object> cacheServicePort) {
        return new OrderService(orderRepositoryPort, productServicePort, sendMessagePort, authServicePort,
                cacheServicePort);
    }
}
