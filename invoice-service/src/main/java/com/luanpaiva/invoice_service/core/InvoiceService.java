package com.luanpaiva.invoice_service.core;

import com.luanpaiva.invoice_service.adapters.out.model.OrderDTO;
import com.luanpaiva.invoice_service.core.model.Invoice;
import com.luanpaiva.invoice_service.core.ports.in.InvoiceServicePort;
import com.luanpaiva.invoice_service.core.ports.out.AuthServicePort;
import com.luanpaiva.invoice_service.core.ports.out.OrderServiceApiPort;

import java.util.UUID;

public class InvoiceService implements InvoiceServicePort {

    private final OrderServiceApiPort orderServiceApiPort;
    private final AuthServicePort authServicePort;

    public InvoiceService(OrderServiceApiPort orderServiceApiPort, AuthServicePort authServicePort) {
        this.orderServiceApiPort = orderServiceApiPort;
        this.authServicePort = authServicePort;
    }

    @Override
    public Invoice generateInvoice(UUID orderId) {
        String accessToken = authServicePort.getInternalToken("invoice-service-token");
        OrderDTO orderDTO = orderServiceApiPort.findById(orderId, accessToken);
        // TODO - AQUI FICARIA A LÓGICA PARA A EMISSÃO DA NOTA FISCAL COM OS DADOS DO PEDIDO.
        return Invoice.builder()
                .orderId(orderDTO.getId())
                .customerEmail(orderDTO.getCustomerEmail())
                .items(orderDTO.getItems())
                .totalAmount(orderDTO.getTotalAmount())
                .build();
    }
}
