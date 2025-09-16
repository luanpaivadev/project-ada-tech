package com.luanpaiva.order_service.core.model;

import com.luanpaiva.order_service.adapters.out.repository.model.DeliveryAddress;
import com.luanpaiva.order_service.adapters.out.repository.model.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    private UUID id;
    private String customerEmail;
    private List<OrderItem> items;
    private DeliveryAddress address;
    private UUID paymentId;
    private PaymentMethod paymentMethod;
    private String paymentMessage;
    private BigDecimal totalAmount;
    private StatusOrder status;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public Order(UUID id) {
        this.id = id;
    }
}
