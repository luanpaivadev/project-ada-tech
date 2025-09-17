package com.luanpaiva.notification_service.core.ports.out;

import com.luanpaiva.notification_service.adapters.in.listener.model.OrderDTO;

public interface NotificationServicePort {

    void notify(OrderDTO order);
}
