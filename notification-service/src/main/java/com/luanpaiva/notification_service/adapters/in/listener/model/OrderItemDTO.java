package com.luanpaiva.notification_service.adapters.in.listener.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class OrderItemDTO {

    private UUID id;
    private ProductDTO product;
    private Integer quantity;
    private BigDecimal amount;
}
