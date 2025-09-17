package com.luanpaiva.order_service.adapters.in.model;

import com.luanpaiva.order_service.adapters.out.repository.model.DeliveryAddress;
import com.luanpaiva.order_service.adapters.out.repository.model.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderInput {

    private List<OrderItemInput> items;
    private DeliveryAddress address;
    private PaymentMethod paymentMethod;
}
