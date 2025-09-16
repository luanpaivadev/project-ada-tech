package com.luanpaiva.invoice_service.core.ports.out;

@FunctionalInterface
public interface SendMessagePort {

    <T> void send(String routingKey, Object object, Class<T> clazz);
}
