package com.luanpaiva.auth_service.core.ports;

import com.luanpaiva.auth_service.core.model.ServiceClient;

import java.util.Optional;

public interface ServiceClientRepositoryPort {

    Optional<ServiceClient> findByClientId(String clientId);
}
