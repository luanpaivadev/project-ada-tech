package com.luanpaiva.auth_service.adapters.out.repository;

import com.luanpaiva.auth_service.adapters.out.repository.model.ServiceClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ServiceClientRepositoryJpa extends JpaRepository<ServiceClientEntity, UUID> {

    Optional<ServiceClientEntity> findByClientId(String username);
}
