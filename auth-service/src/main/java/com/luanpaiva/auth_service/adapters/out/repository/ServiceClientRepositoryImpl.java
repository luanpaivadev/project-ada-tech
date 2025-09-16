package com.luanpaiva.auth_service.adapters.out.repository;

import com.luanpaiva.auth_service.core.model.ServiceClient;
import com.luanpaiva.auth_service.core.ports.ServiceClientRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ServiceClientRepositoryImpl implements ServiceClientRepositoryPort {

    private final ServiceClientRepositoryJpa repositoryJpa;
    private final ModelMapper mapper;

    @Override
    public Optional<ServiceClient> findByClientId(String clientId) {
        return repositoryJpa.findByClientId(clientId)
                .map(serviceClientEntity -> mapper.map(serviceClientEntity, ServiceClient.class));
    }
}
