package com.luanpaiva.product_service.adapters.in.listener.amqp.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@ToString
public class OrderItemDTO {

    private UUID id;
    private ProductDTO product;
    private Integer quantity;
    private BigDecimal amount;
}
