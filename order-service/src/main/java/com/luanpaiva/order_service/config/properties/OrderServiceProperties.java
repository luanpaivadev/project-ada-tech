package com.luanpaiva.order_service.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(value = "order-service")
public class OrderServiceProperties {

    private String clientId;
    private String clientSecret;
}
