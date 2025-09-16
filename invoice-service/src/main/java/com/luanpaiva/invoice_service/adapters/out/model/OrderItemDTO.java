package com.luanpaiva.invoice_service.adapters.out.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class OrderItemDTO {

    private ProductDTO product;
    private Integer quantity;
    private BigDecimal amount;
}
