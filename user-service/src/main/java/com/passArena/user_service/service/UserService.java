package com.passArena.user_service.service;

import com.passArena.user_service.dto.CreateUserRequest;
import com.passArena.user_service.dto.UserResponse;

import java.util.UUID;

public interface UserService {

    UserResponse createUser(CreateUserRequest request);

    UserResponse getUserById(UUID userId);

    UserResponse getUserByEmail(String email);
}
