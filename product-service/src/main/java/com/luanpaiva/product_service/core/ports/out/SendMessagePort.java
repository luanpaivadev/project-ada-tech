package com.luanpaiva.product_service.core.ports.out;

@FunctionalInterface
public interface SendMessagePort {

    <T> void send(String routingKey, Object object, Class<T> clazz);
}
