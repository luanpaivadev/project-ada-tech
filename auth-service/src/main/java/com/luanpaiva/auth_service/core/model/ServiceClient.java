package com.luanpaiva.auth_service.core.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ServiceClient {

    private UUID id;
    private String clientId;
    private String clientSecret;
    private String roles;
}
