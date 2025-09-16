package com.luanpaiva.auth_service.adapters.out.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CustomerDTO {

    private UUID id;
    private String name;
    private String phone;
    private String email;
}
