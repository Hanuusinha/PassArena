package com.passArena.auth_service.controller;

import com.passArena.auth_service.dto.*;
import com.passArena.auth_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignupRequest signupRequest)
    {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.signup(signupRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest)
    {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest)
    {
        return ResponseEntity.ok(authService.refreshTokens(refreshTokenRequest.getRefreshToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody LogoutRequest logoutRequest)
    {
        authService.logout(logoutRequest.getRefreshToken());
        return ResponseEntity.ok("Logout successful");
    }


}
