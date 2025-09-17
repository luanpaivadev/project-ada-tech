package com.luanpaiva.notification_service.adapters.out;

import com.luanpaiva.notification_service.adapters.in.listener.model.OrderDTO;
import com.luanpaiva.notification_service.core.ports.out.NotificationServicePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailNotificationService implements NotificationServicePort {

    @Override
    public void notify(OrderDTO order) {
        switch (order.getStatus()) {
            case CANCELLED -> sendingNotification(order);
            case PAYMENT_PENDING -> sendingNotificationPaymentPending(order);
            case PAYMENT_APPROVED -> sendingNotificationPaymentApproved(order);
            case PAYMENT_FAILURE -> sendingNotificationPaymentFailure(order);
            case PAYMENT_REJECTED -> sendingNotificationPaymentRejected(order);
            case IN_SEPARATION -> sendingNotificationInSeparation(order);
            case TAX_INVOICE_ISSUED -> sendingNotificationTaxInvoiceIssued(order);
            case IN_DELIVERY -> sendingNotificationInDelivery(order);
            case DELIVERED -> sendingNotificationDelivered(order);
        }
    }

    private void sendingNotificationPaymentPending(OrderDTO order) {
        sendingNotification(order);
    }

    private void sendingNotificationPaymentApproved(OrderDTO order) {
        sendingNotification(order);
    }

    private void sendingNotificationPaymentFailure(OrderDTO order) {
        sendingNotification(order);
    }

    private void sendingNotificationPaymentRejected(OrderDTO order) {
        sendingNotification(order);
    }

    private void sendingNotificationInSeparation(OrderDTO order) {
        sendingNotification(order);
    }

    private void sendingNotificationTaxInvoiceIssued(OrderDTO order) {
        sendingNotification(order);
    }

    private void sendingNotificationInDelivery(OrderDTO order) {
        sendingNotification(order);
    }

    private void sendingNotificationDelivered(OrderDTO order) {
        sendingNotification(order);
    }

    private static void sendingNotification(OrderDTO order) {
        log.info("Order ID: {} - Status: {}", order.getId(), order.getStatus());
    }
}
