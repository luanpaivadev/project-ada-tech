package com.luanpaiva.order_service.adapters.out.gateway.payment;

import com.luanpaiva.order_service.config.properties.PaymentGatewayProperties;
import com.luanpaiva.order_service.core.exception.BadRequestException;
import com.luanpaiva.order_service.core.ports.out.PaymentGatewayServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
@RequiredArgsConstructor
public class PaymentGatewayService implements PaymentGatewayServicePort {

    private final PaymentGatewayProperties properties;

    @Override
    public void validateClientSecret(String clientSecret) {
        if (isBlank(clientSecret)) {
            throw new BadRequestException("Payment gateway client secret not found");
        }
        if (!Objects.equals(properties.getClientSecret(), clientSecret)) {
            throw new BadRequestException("Invalid payment gateway client secret");
        }
    }
}
