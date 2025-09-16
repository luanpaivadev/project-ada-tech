package com.luanpaiva.auth_service.adapters.out.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.luanpaiva.auth_service.core.exceptions.InvalidTokenException;
import com.luanpaiva.auth_service.core.model.Customer;
import com.luanpaiva.auth_service.core.model.ServiceClient;
import com.luanpaiva.auth_service.core.ports.Auth0JwtPort;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class Auth0JwtImpl implements Auth0JwtPort {

    private static final int EXPIRATION_HOURS = 1;
    private static final String OFFSET_ID = "-03:00";
    private static final String TECHBRA_API = "TECHBRA API";
    private static final String ROLES = "roles";

    @Value("${jwt.private-key-path}")
    private String privateKeyPath;

    private RSAPrivateKey privateKey;

    @PostConstruct
    public void init() throws Exception {
        String pem = Files.readString(Paths.get(privateKeyPath));
        pem = pem.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        byte[] keyBytes = Base64.getDecoder().decode(pem);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        this.privateKey = (RSAPrivateKey) kf.generatePrivate(spec);
    }

    @Override
    public String getInternalToken(ServiceClient client) {
        try {
            return JWT.create()
                    .withIssuer(TECHBRA_API)
                    .withSubject(client.getClientId())
                    .withClaim(ROLES, client.getRoles())
                    .withIssuedAt(issuedAt())
                    .withExpiresAt(expiresAt())
                    .withJWTId(UUID.randomUUID().toString())
                    .sign(Algorithm.RSA256(null, privateKey));
        } catch (JWTCreationException e) {
            throw new InvalidTokenException("Invalid token.");
        }
    }

    @Override
    public String getLoginToken(Customer customer) {
        try {
            return JWT.create()
                    .withIssuer(TECHBRA_API)
                    .withSubject(customer.getEmail())
                    .withClaim(ROLES, customer.getRoles())
                    .withIssuedAt(issuedAt())
                    .withExpiresAt(expiresAt())
                    .withJWTId(UUID.randomUUID().toString())
                    .sign(Algorithm.RSA256(null, privateKey));
        } catch (JWTCreationException e) {
            throw new InvalidTokenException("Invalid token.");
        }
    }

    private Instant issuedAt() {
        return LocalDateTime.now().toInstant(ZoneOffset.of(OFFSET_ID));
    }

    private Instant expiresAt() {
        return LocalDateTime.now().plusHours(EXPIRATION_HOURS).toInstant(ZoneOffset.of(OFFSET_ID));
    }
}
