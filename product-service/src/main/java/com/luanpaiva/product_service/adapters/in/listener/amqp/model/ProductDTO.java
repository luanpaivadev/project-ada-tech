package com.luanpaiva.product_service.adapters.in.listener.amqp.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@ToString
public class ProductDTO {

    private UUID id;
    private UUID productId;
    private String name;
    private String description;
    private BigDecimal price;
}
