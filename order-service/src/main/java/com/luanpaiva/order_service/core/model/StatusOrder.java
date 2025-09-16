package com.luanpaiva.order_service.core.model;

public enum StatusOrder {

    CANCELLED,
    PAYMENT_PENDING,
    PAYMENT_APPROVED,
    PAYMENT_FAILURE,
    PAYMENT_REJECTED,
    IN_SEPARATION,
    TAX_INVOICE_ISSUED,
    IN_DELIVERY,
    DELIVERED
}
