package com.luanpaiva.order_service.core.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenPayload {

    private String name;
    private String email;

    public TokenPayload(String email) {
        this.email = email;
    }
}
