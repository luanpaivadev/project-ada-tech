package com.luanpaiva.auth_service.adapters.out.repository;

import com.luanpaiva.auth_service.core.model.Customer;
import com.luanpaiva.auth_service.core.ports.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryPort {

    private final UserRepositoryJpa repositoryJpa;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<Customer> findByEmail(String email) {
        return repositoryJpa.findByEmail(email)
                .map(customerEntity -> mapper.map(customerEntity, Customer.class));
    }

    @Override
    public boolean validatePassword(CharSequence rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
