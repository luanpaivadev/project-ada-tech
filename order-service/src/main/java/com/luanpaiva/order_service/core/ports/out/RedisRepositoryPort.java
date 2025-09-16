package com.luanpaiva.order_service.core.ports.out;

public interface RedisRepositoryPort {

    void save(String key, Object value);

    Object get(String key);
}
