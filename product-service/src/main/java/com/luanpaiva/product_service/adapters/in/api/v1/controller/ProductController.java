package com.luanpaiva.product_service.adapters.in.api.v1.controller;

import com.luanpaiva.product_service.adapters.out.model.ProductDTO;
import com.luanpaiva.product_service.core.model.Product;
import com.luanpaiva.product_service.core.ports.in.ProductServicePort;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/products")
public class ProductController {

    private final ProductServicePort productServicePort;
    private final ModelMapper mapper;

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> findAllProducts(Pageable pageable) {
        Page<Product> products = productServicePort.findAll(pageable);
        Page<ProductDTO> productDTOS = products.map(product -> mapper.map(product, ProductDTO.class));
        return ResponseEntity.ok(productDTOS);
    }

    @GetMapping("/check-product-inventory/product/{productId}/quantity/{quantity}")
    public ResponseEntity<ProductDTO> checkProductInventory(@PathVariable UUID productId,
                                                            @PathVariable Integer quantity) {
        Product product = productServicePort.checkProductInventory(productId, quantity);
        ProductDTO productDTO = mapper.map(product, ProductDTO.class);
        return ResponseEntity.ok(productDTO);
    }
}
