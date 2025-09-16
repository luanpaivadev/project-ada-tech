package com.luanpaiva.order_service.core.ports.out;

@FunctionalInterface
public interface SendMessagePort {

    <T> void send(String routingKey, Object object, Class<T> clazz);
}
