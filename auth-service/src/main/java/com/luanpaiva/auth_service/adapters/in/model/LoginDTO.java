package com.luanpaiva.auth_service.adapters.in.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {

    private String email;
    private String password;
}
