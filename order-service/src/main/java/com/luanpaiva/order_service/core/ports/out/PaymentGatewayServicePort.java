package com.luanpaiva.order_service.core.ports.out;

public interface PaymentGatewayServicePort {

    Boolean validateClientSecret(String clientSecret);
}
