package com.luanpaiva.invoice_service.adapters.in.api.v1.controller;

import com.luanpaiva.invoice_service.adapters.out.model.InvoiceDTO;
import com.luanpaiva.invoice_service.core.model.Invoice;
import com.luanpaiva.invoice_service.core.ports.in.InvoiceServicePort;
import com.luanpaiva.invoice_service.core.ports.out.SendMessagePort;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/invoices")
public class InvoiceController {

    private final InvoiceServicePort invoiceServicePort;
    private final ModelMapper mapper;
    private final SendMessagePort sendMessagePort;

    @PostMapping
    public ResponseEntity<InvoiceDTO> generateInvoice(@RequestParam UUID orderId) {
        Invoice invoice = invoiceServicePort.generateInvoice(orderId);
        InvoiceDTO invoiceDTO = mapper.map(invoice, InvoiceDTO.class);
        sendMessagePort.send("order.tax_invoice_issued", invoiceDTO, null);
        return ResponseEntity.status(HttpStatus.CREATED).body(invoiceDTO);
    }
}
