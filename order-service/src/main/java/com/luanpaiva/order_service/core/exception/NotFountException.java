package com.luanpaiva.order_service.core.exception;

public class NotFountException extends RuntimeException {
    public NotFountException() {
        super();
    }

    public NotFountException(String message) {
        super(message);
    }
}
