package com.luanpaiva.product_service.config.security;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.luanpaiva.product_service.core.ports.out.AuthServicePort;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION = "Authorization";
    private final AuthServicePort authServicePort;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String bearerToken = getToken(request);
        if (nonNull(bearerToken)) {
            DecodedJWT decodedJWT = authServicePort.validateToken(bearerToken);
            var authenticationToken = new UsernamePasswordAuthenticationToken(decodedJWT.getSubject(), null, getAuthorities(decodedJWT));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String authorization = request.getHeader(AUTHORIZATION);
        if (isNotBlank(authorization)) {
            authorization = authorization.replace("Bearer ", "");
            return authorization;
        }
        return null;
    }

    private static List<SimpleGrantedAuthority> getAuthorities(DecodedJWT decodedJWT) {
        Claim roles = decodedJWT.getClaim("roles");
        return Arrays.stream(roles.asString().split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
}
