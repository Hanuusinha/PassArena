package com.passArena.auth_service.service;

import com.passArena.auth_service.client.UserServiceClient;
import com.passArena.auth_service.repository.RefreshTokenRepository;
import com.passArena.auth_service.repository.UserCredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserCredentialRepository userCredentialRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserServiceClient userServiceClient;
    private final JwtService jwtService;


}
