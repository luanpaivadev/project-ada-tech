package com.luanpaiva.invoice_service.core.ports.out;

import com.luanpaiva.invoice_service.adapters.out.model.OrderDTO;

import java.util.UUID;

public interface OrderServiceApiPort {

    OrderDTO findById(UUID orderId, String bearerToken);
}
