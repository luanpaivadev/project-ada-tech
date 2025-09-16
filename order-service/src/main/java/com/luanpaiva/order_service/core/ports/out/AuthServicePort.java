package com.luanpaiva.order_service.core.ports.out;

import com.auth0.jwt.interfaces.DecodedJWT;

public interface AuthServicePort {

    DecodedJWT validateToken(String bearerToken);

    String getInternalToken(String key);
}
