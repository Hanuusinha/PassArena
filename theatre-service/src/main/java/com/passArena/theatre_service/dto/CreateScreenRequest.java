package com.passArena.theatre_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateScreenRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String screenType;

    @NotNull
    @Min(1)
    private Integer totalSeats;
}
