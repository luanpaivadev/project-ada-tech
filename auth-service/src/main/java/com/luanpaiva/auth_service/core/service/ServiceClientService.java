package com.luanpaiva.auth_service.core.service;

import com.luanpaiva.auth_service.core.exceptions.BadRequestException;
import com.luanpaiva.auth_service.core.exceptions.NotFoundException;
import com.luanpaiva.auth_service.core.model.ServiceClient;
import com.luanpaiva.auth_service.core.ports.ServiceClientRepositoryPort;
import com.luanpaiva.auth_service.core.ports.ServiceClientServicePort;
import org.springframework.security.crypto.password.PasswordEncoder;

public class ServiceClientService implements ServiceClientServicePort {

    private final ServiceClientRepositoryPort repositoryPort;
    private final PasswordEncoder passwordEncoder;

    public ServiceClientService(ServiceClientRepositoryPort repositoryPort, PasswordEncoder passwordEncoder) {
        this.repositoryPort = repositoryPort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ServiceClient findByClientId(String clientId, String clientSecret) {
        ServiceClient client = repositoryPort.findByClientId(clientId)
                .orElseThrow(() -> new NotFoundException("Client not found."));
        if (passwordEncoder.matches(clientSecret, client.getClientSecret())) {
            return client;
        }
        throw new BadRequestException("Inválid client secret.");
    }
}
