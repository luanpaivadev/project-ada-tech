package com.luanpaiva.order_service.adapters.out.handler;

import com.luanpaiva.order_service.core.exception.BadRequestException;
import com.luanpaiva.order_service.core.exception.InternalServerErrorException;
import com.luanpaiva.order_service.core.exception.InvalidTokenException;
import com.luanpaiva.order_service.core.exception.NotFountException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({Exception.class, RuntimeException.class, InternalServerErrorException.class})
    public ResponseEntity<String> internalServerErrorExceptionHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    @ExceptionHandler({NotFountException.class})
    public ResponseEntity<String> notFoundExceptionHandler(NotFountException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler({InvalidTokenException.class, BadRequestException.class})
    public ResponseEntity<String> badRequestExceptionHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
