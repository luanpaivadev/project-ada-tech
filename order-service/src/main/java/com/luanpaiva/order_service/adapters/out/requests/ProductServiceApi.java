package com.luanpaiva.order_service.adapters.out.requests;

import com.luanpaiva.order_service.adapters.out.model.ProductDTO;
import com.luanpaiva.order_service.config.properties.ProductServiceApiProperties;
import com.luanpaiva.order_service.core.exception.InternalServerErrorException;
import com.luanpaiva.order_service.core.ports.out.ProductServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import static java.text.MessageFormat.format;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductServiceApi implements ProductServicePort {

    private final ProductServiceApiProperties properties;

    @Override
    public ProductDTO checkProductInventory(UUID productID, Integer quantity, String accessToken) {
        try {
            String host = properties.getHost();
            String checkProductInventory = properties.getCheckProductInventory();
            URI uri = new URI(format((host + checkProductInventory), productID, quantity));
            return RestClient.create().get()
                    .uri(uri)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .retrieve()
                    .body(ProductDTO.class);
        } catch (URISyntaxException | RestClientException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
