package com.luanpaiva.product_service.adapters.out.handler;

import com.luanpaiva.product_service.adapters.out.model.ResponseErrorDTO;
import com.luanpaiva.product_service.core.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({Exception.class, RuntimeException.class})
    public ResponseEntity<ResponseErrorDTO> internalServerErrorException(Exception e) {
        ResponseErrorDTO responseErrorDTO = new ResponseErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseErrorDTO);
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ResponseErrorDTO> notFoundException(NotFoundException e) {
        ResponseErrorDTO responseErrorDTO = new ResponseErrorDTO(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseErrorDTO);
    }
}
