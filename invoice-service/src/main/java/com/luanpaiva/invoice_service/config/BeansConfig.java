package com.luanpaiva.invoice_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.luanpaiva.invoice_service.core.InvoiceService;
import com.luanpaiva.invoice_service.core.ports.in.InvoiceServicePort;
import com.luanpaiva.invoice_service.core.ports.out.AuthServicePort;
import com.luanpaiva.invoice_service.core.ports.out.OrderServiceApiPort;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public InvoiceServicePort invoiceServicePort(OrderServiceApiPort orderServiceApiPort,
                                                 AuthServicePort authServicePort) {
        return new InvoiceService(orderServiceApiPort, authServicePort);
    }
}
