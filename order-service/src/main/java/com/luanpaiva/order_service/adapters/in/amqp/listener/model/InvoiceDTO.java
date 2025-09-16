package com.luanpaiva.order_service.adapters.in.amqp.listener.model;

import com.luanpaiva.order_service.adapters.out.model.OrderItemDTO;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class InvoiceDTO {

    private UUID orderId;
    private String customerEmail;
    private List<OrderItemDTO> items;
    private BigDecimal totalAmount;
}
