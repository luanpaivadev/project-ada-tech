package com.luanpaiva.product_service.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(value = "spring-datasource")
public class DataSourceProperties {

    private String url;
    private String username;
    private String password;
    private String driverClassName;
    private Integer maximumPoolSize;
    private Integer minimumIdle;
    private Long idleTimeout;
}
