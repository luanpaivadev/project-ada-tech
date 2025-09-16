package com.luanpaiva.auth_service.core.ports;

import com.luanpaiva.auth_service.core.model.Customer;
import com.luanpaiva.auth_service.core.model.ServiceClient;

public interface Auth0JwtPort {

    String getInternalToken(ServiceClient client);

    String getLoginToken(Customer customer);
}
