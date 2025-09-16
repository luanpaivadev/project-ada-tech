package com.luanpaiva.order_service.adapters.in.model;

import com.luanpaiva.order_service.core.model.StatusPayment;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PaymentWebhookPayload {

    private UUID orderId;
    private UUID paymentId;
    private StatusPayment statusPayment;
    private String message;
}
