package com.luanpaiva.order_service.core.ports.out;

public interface PaymentGatewayServicePort {

    void validateClientSecret(String clientSecret);
}
