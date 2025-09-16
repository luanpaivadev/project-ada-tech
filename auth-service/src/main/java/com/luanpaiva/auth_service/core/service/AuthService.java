package com.luanpaiva.auth_service.core.service;

import com.luanpaiva.auth_service.core.exceptions.BadRequestException;
import com.luanpaiva.auth_service.core.model.Customer;
import com.luanpaiva.auth_service.core.model.ServiceClient;
import com.luanpaiva.auth_service.core.ports.Auth0JwtPort;
import com.luanpaiva.auth_service.core.ports.AuthServicePort;
import com.luanpaiva.auth_service.core.ports.UserServicePort;

public class AuthService implements AuthServicePort {

    private final UserServicePort userServicePort;
    private final Auth0JwtPort auth0JwtPort;

    public AuthService(UserServicePort userServicePort, Auth0JwtPort auth0JwtPort) {
        this.userServicePort = userServicePort;
        this.auth0JwtPort = auth0JwtPort;
    }

    @Override
    public String getInternalToken(ServiceClient client) {
        return auth0JwtPort.getInternalToken(client);
    }

    @Override
    public String getLoginToken(String email, String password) {
        Customer customer = userServicePort.findByEmail(email);
        if (userServicePort.validatePassword(password, customer.getPassword())) {
            return auth0JwtPort.getLoginToken(customer);
        }
        throw new BadRequestException("Invalid email or password.");
    }
}
