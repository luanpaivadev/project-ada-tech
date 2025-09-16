package com.luanpaiva.auth_service.core.service;

import com.luanpaiva.auth_service.core.exceptions.NotFoundException;
import com.luanpaiva.auth_service.core.model.Customer;
import com.luanpaiva.auth_service.core.ports.UserRepositoryPort;
import com.luanpaiva.auth_service.core.ports.UserServicePort;

public class UserService implements UserServicePort {

    private final UserRepositoryPort userRepositoryPort;

    public UserService(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public Customer findByEmail(String email) {
        return userRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found."));
    }

    @Override
    public boolean validatePassword(CharSequence rawPassword, String encodedPassword) {
        return userRepositoryPort.validatePassword(rawPassword, encodedPassword);
    }
}
