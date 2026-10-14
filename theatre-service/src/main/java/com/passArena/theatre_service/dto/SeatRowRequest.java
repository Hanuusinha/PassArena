package com.passArena.theatre_service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SeatRowRequest {

    @NotBlank
    private String rowNumber;

    @NotNull
    @Min(1)
    private Integer numberOfSeats;

    @NotNull
    private SeatType seatType;

    @NotNull
    @DecimalMin(value = "0.0")
    private BigDecimal price;
}
