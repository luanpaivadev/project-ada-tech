package com.luanpaiva.invoice_service.adapters.out.api;

import com.luanpaiva.invoice_service.adapters.out.model.OrderDTO;
import com.luanpaiva.invoice_service.config.properties.OrderServiceProperties;
import com.luanpaiva.invoice_service.core.exception.InternalServerErrorException;
import com.luanpaiva.invoice_service.core.ports.out.OrderServiceApiPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.UUID;

import static java.text.MessageFormat.format;

@Component
@RequiredArgsConstructor
public class OrderServiceApi implements OrderServiceApiPort {

    private final OrderServiceProperties properties;

    @Override
    public OrderDTO findById(UUID orderId, String bearerToken) {
        try {
            String host = properties.getHost();
            String findByIdUrl = properties.getFindOrderByIdUrl();
            URI uri = new URI(format((host + findByIdUrl), orderId));
            return RestClient.create().get()
                    .uri(uri)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                    .retrieve()
                    .body(OrderDTO.class);
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
