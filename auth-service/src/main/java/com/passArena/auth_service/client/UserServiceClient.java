package com.passArena.auth_service.client;

import com.passArena.auth_service.dto.CreateUserRequest;
import com.passArena.auth_service.dto.UserResponse;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Component
public class UserServiceClient {
    private final RestClient restClient;

    public UserServiceClient(@LoadBalanced RestClient.Builder builder)
    {
        this.restClient = builder
                .baseUrl("http://user-service")
                .build();
    }

    public UserResponse createUser(CreateUserRequest request)
    {
        return restClient
                .post()
                .uri("/users")
                .body(request)
                .retrieve()
                .body(UserResponse.class);
    }

    public UserResponse getUserByEmail(String email)
    {
        return restClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/users/email/{email}")
                                .build(email))
                .retrieve()
                .body(UserResponse.class);
    }

    public UserResponse getUserById(String userId)
    {
        return restClient
                .get()
                .uri("/users/{userId}",userId)
                .retrieve()
                .body(UserResponse.class);
    }

    public void deleteUser(UUID userId)
    {
        restClient
                .delete()
                .uri("/users/{userId}", userId)
                .retrieve()
                .toBodilessEntity();
    }
}
