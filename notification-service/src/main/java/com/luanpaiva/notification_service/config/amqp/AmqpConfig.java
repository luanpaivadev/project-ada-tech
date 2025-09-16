package com.luanpaiva.notification_service.config.amqp;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfig {

    @Bean
    public Queue statusOrderNotificationQueue() {
        return new Queue("notification.status_order", true);
    }
}
