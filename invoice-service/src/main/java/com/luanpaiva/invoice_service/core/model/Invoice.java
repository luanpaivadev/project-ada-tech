package com.luanpaiva.invoice_service.core.model;

import com.luanpaiva.invoice_service.adapters.out.model.OrderItemDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {

    private UUID orderId;
    private String customerEmail;
    private List<OrderItemDTO> items;
    private BigDecimal totalAmount;
}
