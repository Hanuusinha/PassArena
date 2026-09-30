package com.passArena.user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email")
    private String email;

    @NotBlank(message = "FirstName is required")
    @Size(max = 100)
    private String firstName;

    @NotBlank(message = "LastName is required ")
    @Size(max = 100)
    private String lastName;

    @Size(max = 50)
    private String phone;
}
