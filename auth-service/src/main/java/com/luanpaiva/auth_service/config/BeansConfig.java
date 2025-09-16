package com.luanpaiva.auth_service.config;

import com.luanpaiva.auth_service.core.ports.Auth0JwtPort;
import com.luanpaiva.auth_service.core.ports.AuthServicePort;
import com.luanpaiva.auth_service.core.ports.ServiceClientRepositoryPort;
import com.luanpaiva.auth_service.core.ports.ServiceClientServicePort;
import com.luanpaiva.auth_service.core.ports.UserRepositoryPort;
import com.luanpaiva.auth_service.core.ports.UserServicePort;
import com.luanpaiva.auth_service.core.service.AuthService;
import com.luanpaiva.auth_service.core.service.ServiceClientService;
import com.luanpaiva.auth_service.core.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BeansConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public UserServicePort userServicePort(UserRepositoryPort userRepositoryPort) {
        return new UserService(userRepositoryPort);
    }

    @Bean
    public AuthServicePort authServicePort(UserServicePort userServicePort, Auth0JwtPort auth0JwtPort) {
        return new AuthService(userServicePort, auth0JwtPort);
    }

    @Bean
    public ServiceClientServicePort serviceClientServicePort(ServiceClientRepositoryPort repositoryPort,
                                                             PasswordEncoder passwordEncoder) {
        return new ServiceClientService(repositoryPort, passwordEncoder);
    }
}
