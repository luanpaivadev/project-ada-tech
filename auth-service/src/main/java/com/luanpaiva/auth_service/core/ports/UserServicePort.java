package com.luanpaiva.auth_service.core.ports;

import com.luanpaiva.auth_service.core.model.Customer;

public interface UserServicePort {

    Customer findByEmail(String email);

    boolean validatePassword(CharSequence rawPassword, String encodedPassword);
}
