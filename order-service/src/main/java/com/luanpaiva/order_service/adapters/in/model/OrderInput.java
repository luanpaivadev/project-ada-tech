package com.luanpaiva.order_service.adapters.in.model;

import com.luanpaiva.order_service.adapters.out.repository.model.DeliveryAddress;
import com.luanpaiva.order_service.adapters.out.repository.model.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderInput {

    private List<OrderItemInput> items;
    private DeliveryAddress address;
    private PaymentMethod paymentMethod;
}
