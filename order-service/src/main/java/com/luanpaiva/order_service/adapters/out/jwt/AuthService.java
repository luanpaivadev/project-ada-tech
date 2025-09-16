package com.luanpaiva.order_service.adapters.out.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.luanpaiva.order_service.adapters.out.requests.response.BearerToken;
import com.luanpaiva.order_service.config.properties.AuthServiceProperties;
import com.luanpaiva.order_service.config.properties.OrderServiceProperties;
import com.luanpaiva.order_service.core.exception.InternalServerErrorException;
import com.luanpaiva.order_service.core.ports.out.AuthServicePort;
import com.luanpaiva.order_service.core.ports.out.CacheServicePort;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static java.text.MessageFormat.format;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthService implements AuthServicePort {

    @Value("${jwt.public-key-path}")
    private String publicKeyPath;

    private final CacheServicePort<String, Object> cacheServicePort;
    private final AuthServiceProperties authServiceProperties;
    private final OrderServiceProperties orderServiceProperties;

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
        try {
            return JWT.require(Algorithm.RSA256(publicKey, null))
                    .build()
                    .verify(bearerToken);
        } catch (TokenExpiredException e) {
            return null;
        }
    }

    @Override
    public String getInternalToken(String key) {
        try {
            Object accessToken = cacheServicePort.get(key);
            if (isNull(accessToken)) {
                String host = authServiceProperties.getHost();
                String getInternalTokenUrl = authServiceProperties.getGetInternalTokenUrl();
                String clientId = orderServiceProperties.getClientId();
                String clientSecret = orderServiceProperties.getClientSecret();
                String url = format((host + getInternalTokenUrl), clientId, clientSecret);
                BearerToken response = RestClient.create().get()
                        .uri(new URI(url))
                        .retrieve()
                        .body(BearerToken.class);
                if (nonNull(response)) {
                    String bearerToken = response.getAccessToken();
                    cacheServicePort.set(key, bearerToken);
                    return bearerToken;
                }
                throw new InternalServerErrorException("Error obtaining internal token.");
            }
            DecodedJWT decodedJWT = validateToken((String) accessToken);
            if (isNull(decodedJWT)) {
                cacheServicePort.delete(key);
                return getInternalToken(key);
            }
            return (String) accessToken;
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
