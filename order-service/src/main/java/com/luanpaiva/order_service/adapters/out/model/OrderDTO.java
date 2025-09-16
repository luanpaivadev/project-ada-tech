package com.luanpaiva.order_service.adapters.out.model;

import com.luanpaiva.order_service.core.model.StatusOrder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class OrderDTO {

    private UUID id;
    private String customerEmail;
    private List<OrderItemDTO> items;
    private BigDecimal totalAmount;
    private StatusOrder status;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
