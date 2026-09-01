package com.passArena.auth_service.service;

import com.passArena.auth_service.client.UserServiceClient;
import com.passArena.auth_service.dto.*;
import com.passArena.auth_service.entity.RefreshToken;
import com.passArena.auth_service.entity.UserCredential;
import com.passArena.auth_service.repository.RefreshTokenRepository;
import com.passArena.auth_service.repository.UserCredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserCredentialRepository userCredentialRepository;
    private final UserServiceClient userServiceClient;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse signup(SignupRequest request)
    {
        userCredentialRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            throw new RuntimeException("User already exists");
        });

        CreateUserRequest createUserRequest = CreateUserRequest.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .build();

        UserResponse user = userServiceClient.createUser(createUserRequest);

        try{
            String passwordHash = passwordEncoder.encode(request.getPassword());
            UserCredential userCredential = UserCredential.builder()
                    .userId(user.getId().toString())
                    .email(request.getEmail())
                    .passwordHash(passwordEncoder.encode(request.getPassword()))
                    .role("USER")
                    .accountStatus("ACTIVE")
                    .build();

            userCredentialRepository.save(userCredential);


            //Generate access token:
            String token = jwtService.generateToken(
                    user.getId().toString(),
                    request.getEmail(),
                    "USER"
            );
            //Generate refresh token:

            String refreshToken = refreshTokenService.createRefreshToken(
                    user.getId()
            );

            return AuthResponse.builder()
                    .accessToken(token)
                    .tokenType("Bearer")
                    .expiresIn(900L)
                    .build();


        }catch (Exception e)
        {
            try{
                userServiceClient.deleteUser(user.getId());
            } catch (Exception compensationException) {
                compensationException.printStackTrace();
            }

            throw e;
        }
    }

    public AuthResponse login(LoginRequest request)
    {
        UserResponse userResponse = userServiceClient.getUserByEmail(request.getEmail());
        UserCredential user = userCredentialRepository.findByUserId(userResponse.getId().toString())
                .orElseThrow(() -> new BadCredentialsException("Invalid Credentials"));

        if(!user.getAccountStatus().equals("ACTIVE"))
        {
            throw new BadCredentialsException("Account is not active");
        }

        if(!passwordEncoder.matches(request.getPassword(), user.getPasswordHash()))
        {
            throw new RuntimeException("Invalid Credentials");
        }

        String token = jwtService.generateToken(
                user.getUserId(),
                user.getEmail(),
                user.getRole()
        );

        String refreshToken = refreshTokenService.createRefreshToken(userResponse.getId());

        return AuthResponse.builder()
                .accessToken(token)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(900L)
                .build();



    }

    public AuthResponse refreshTokens(String rawRefreshToken)
    {
        RefreshToken oldToken = refreshTokenService.validateRefreshToken(rawRefreshToken);

        UUID userId = oldToken.getUserId();

        UserCredential credential = userCredentialRepository.findByUserId(userId.toString())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!credential.getAccountStatus().equals("ACTIVE"))
        {
            throw new RuntimeException("Account is not active");
        }

        UserResponse userResponse = userServiceClient.getUserById(userId.toString());

        refreshTokenService.revokeToken(rawRefreshToken);

        String accessToken = jwtService.generateToken(
                userResponse.getId().toString(),
                userResponse.getEmail(),
                credential.getRole()
        );

        String refreshToken = refreshTokenService
                .createRefreshToken(userResponse.getId());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(900L)
                .build();
    }

    public void logout(String rawRefreshToken)
    {
        refreshTokenService.revokeToken(rawRefreshToken);
    }


}
