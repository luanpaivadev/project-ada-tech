package com.luanpaiva.order_service.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(value = "product-service")
public class ProductServiceApiProperties {

    private String host;
    private String checkProductInventory;
}
