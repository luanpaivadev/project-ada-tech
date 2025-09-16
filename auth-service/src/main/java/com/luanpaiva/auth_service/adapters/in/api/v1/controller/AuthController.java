package com.luanpaiva.auth_service.adapters.in.api.v1.controller;

import com.luanpaiva.auth_service.adapters.in.model.LoginDTO;
import com.luanpaiva.auth_service.adapters.out.model.BearerToken;
import com.luanpaiva.auth_service.core.model.ServiceClient;
import com.luanpaiva.auth_service.core.ports.AuthServicePort;
import com.luanpaiva.auth_service.core.ports.ServiceClientServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthServicePort authServicePort;
    private final ServiceClientServicePort servicePort;

    @GetMapping(value = "/internal-token")
    public ResponseEntity<BearerToken> getInternalToken(@RequestParam String clientId,
                                                        @RequestParam String clientSecret) {
        ServiceClient client = servicePort.findByClientId(clientId, clientSecret);
        String accessToken = authServicePort.getInternalToken(client);
        return ResponseEntity.ok(new BearerToken(accessToken));
    }

    @PostMapping(value = "/login")
    public ResponseEntity<BearerToken> login(@RequestBody LoginDTO loginDTO) {
        String accessToken = authServicePort.getLoginToken(loginDTO.getEmail(), loginDTO.getPassword());
        return ResponseEntity.ok(new BearerToken(accessToken));
    }
}
