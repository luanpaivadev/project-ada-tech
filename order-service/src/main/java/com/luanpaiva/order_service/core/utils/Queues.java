package com.luanpaiva.order_service.core.utils;

public abstract class Queues {

    public static final String PRODUCT_PAYMENT_APPROVED_QUEUE = "product.payment_approved";
    public static final String NOTIFICATION_STATUS_ORDER_QUEUE = "notification.status_order";

    Queues() throws IllegalAccessException {
        throw new IllegalAccessException();
    }
}
