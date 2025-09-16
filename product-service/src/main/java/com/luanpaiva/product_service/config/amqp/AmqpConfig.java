package com.luanpaiva.product_service.config.amqp;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfig {

    @Bean
    public Queue productPaymentApprovedQueue() {
        return new Queue("product.payment_approved", true);
    }
}
