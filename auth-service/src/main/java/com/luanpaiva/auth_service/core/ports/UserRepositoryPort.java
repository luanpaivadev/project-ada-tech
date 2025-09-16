package com.luanpaiva.auth_service.core.ports;

import com.luanpaiva.auth_service.core.model.Customer;

import java.util.Optional;

public interface UserRepositoryPort {

    Optional<Customer> findByEmail(String email);

    boolean validatePassword(CharSequence rawPassword, String encodedPassword);
}
