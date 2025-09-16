package com.luanpaiva.invoice_service.core.ports.in;

import com.luanpaiva.invoice_service.core.model.Invoice;

import java.util.UUID;

public interface InvoiceServicePort {

    Invoice generateInvoice(UUID orderId);
}
