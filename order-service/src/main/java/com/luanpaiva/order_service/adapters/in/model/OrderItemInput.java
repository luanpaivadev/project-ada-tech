package com.luanpaiva.order_service.adapters.in.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class OrderItemInput {

    private UUID productId;
    private Integer quantity;
}
