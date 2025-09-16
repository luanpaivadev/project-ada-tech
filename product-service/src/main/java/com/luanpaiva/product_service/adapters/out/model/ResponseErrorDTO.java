package com.luanpaiva.product_service.adapters.out.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ResponseErrorDTO {

    private Integer statusCode;
    private String message;
    private LocalDateTime timestamp;

    public ResponseErrorDTO(Integer statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
