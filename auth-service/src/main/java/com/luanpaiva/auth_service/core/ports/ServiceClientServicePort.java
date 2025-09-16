package com.luanpaiva.auth_service.core.ports;

import com.luanpaiva.auth_service.core.model.ServiceClient;

public interface ServiceClientServicePort {

    ServiceClient findByClientId(String clientId, String clientSecret);
}
