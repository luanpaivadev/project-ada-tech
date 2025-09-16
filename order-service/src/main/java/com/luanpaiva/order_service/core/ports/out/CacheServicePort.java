package com.luanpaiva.order_service.core.ports.out;

public interface CacheServicePort<K, V> {

    V get(K k);

    void set(K k, V v);

    Boolean exists(K k);

    void delete(K k);
}
