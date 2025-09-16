package com.luanpaiva.product_service.core.ports.out;

import com.auth0.jwt.interfaces.DecodedJWT;

public interface AuthServicePort {

    DecodedJWT validateToken(String bearerToken);
}
