package com.luanpaiva.order_service.config.amqp;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfig {

    @Bean
    public Queue orderUpdatedInventory() {
        return new Queue("order.updated_inventory", true);
    }

    @Bean
    public Queue orderInventorySuccessfullyUpdated() {
        return new Queue("order.inventory_successfully_updated", true);
    }

    @Bean
    public Queue orderTaxInvoiceIssued() {
        return new Queue("order.tax_invoice_issued", true);
    }

}
