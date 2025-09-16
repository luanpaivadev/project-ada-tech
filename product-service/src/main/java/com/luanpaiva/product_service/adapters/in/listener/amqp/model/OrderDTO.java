package com.luanpaiva.product_service.adapters.in.listener.amqp.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
public class OrderDTO {

    private UUID id;
    private String customerEmail;
    private List<OrderItemDTO> items;
    private BigDecimal totalAmount;
    private StatusOrder status;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
