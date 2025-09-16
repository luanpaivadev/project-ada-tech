package com.luanpaiva.order_service.adapters.in.amqp.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luanpaiva.order_service.adapters.in.amqp.listener.model.InvoiceDTO;
import com.luanpaiva.order_service.core.model.StatusOrder;
import com.luanpaiva.order_service.core.ports.in.OrderServicePort;
import com.luanpaiva.order_service.core.ports.out.SendMessagePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
@RabbitListener(queues = "order.tax_invoice_issued")
public class TaxInvoiceIssuedListener {

    private final OrderServicePort orderServicePort;
    private final ObjectMapper mapper;
    private final SendMessagePort sendMessagePort;

    @RabbitHandler
    public void orderTaxInvoiceIssued(byte[] message) throws IOException {
        InvoiceDTO invoiceDTO = mapper.readValue(message, InvoiceDTO.class);
        orderServicePort.updateStatusOrder(invoiceDTO.getOrderId(), StatusOrder.TAX_INVOICE_ISSUED);
        sendMessagePort.send("notification.status_order", StatusOrder.TAX_INVOICE_ISSUED, null);
    }
}
