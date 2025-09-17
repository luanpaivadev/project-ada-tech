package com.luanpaiva.order_service.adapters.out.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private UUID id;
    private UUID productId;
    private String name;
    private String description;
    private BigDecimal price;
}
