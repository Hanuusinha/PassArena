package com.passArena.user_service.service.impl;

import com.passArena.user_service.dto.CreateUserRequest;
import com.passArena.user_service.dto.UserResponse;
import com.passArena.user_service.entity.User;
import com.passArena.user_service.entity.UserStatus;
import com.passArena.user_service.exception.UserAlreadyExistsException;
import com.passArena.user_service.exception.UserNotFoundException;
import com.passArena.user_service.repository.UserRepository;
import com.passArena.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserResponse createUser(CreateUserRequest request)
    {
        String email = request.getEmail()
                .trim()
                .toLowerCase();

        if(userRepository.existsByEmail(email))
        {
            throw new UserAlreadyExistsException("User already exists with email id: " + email);
        }

        User user = User.builder()
                .email(email)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .status(UserStatus.ACTIVE)
                .build();

        user = userRepository.save(user);

        return mapToResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(UUID userId)
    {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        return mapToResponse(user);
    }

    @Override
    @Transactional
    public UserResponse getUserByEmail(String email)
    {
        User user = userRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new UserNotFoundException("User not found with email : " + email));

        return mapToResponse(user);
    }

    private UserResponse mapToResponse(User user)
    {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .status(user.getStatus().name())
                .build();
    }
}
