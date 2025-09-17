package com.luanpaiva.notification_service.adapters.in.listener.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class ProductDTO {

    private UUID id;
    private UUID productId;
    private String name;
    private String description;
    private BigDecimal price;
}
