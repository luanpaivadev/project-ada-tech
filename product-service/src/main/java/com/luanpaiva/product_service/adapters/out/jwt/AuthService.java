package com.luanpaiva.product_service.adapters.out.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.luanpaiva.product_service.core.ports.out.AuthServicePort;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthService implements AuthServicePort {

    @Value("${jwt.public-key-path}")
    private String publicKeyPath;

    private RSAPublicKey publicKey;

    @PostConstruct
    public void init() throws Exception {
        String pem = Files.readString(Paths.get(publicKeyPath))
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        byte[] decoded = Base64.getDecoder().decode(pem);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        this.publicKey = (RSAPublicKey) kf.generatePublic(new X509EncodedKeySpec(decoded));
    }

    @Override
    public DecodedJWT validateToken(String bearerToken) {
        return JWT.require(Algorithm.RSA256(publicKey, null))
                .build()
                .verify(bearerToken);
    }
}
