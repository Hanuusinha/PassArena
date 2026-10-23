package com.passArena.theatre_service.dto;

import com.passArena.theatre_service.entity.SeatType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateSeatRequest {

    @NotBlank
    private String seatNumber;

    @NotNull
    private SeatType seatType;

    @NotBlank
    private String rowNumber;

    @NotNull
    @DecimalMin(value = "0.0")
    private BigDecimal seatPrice;
}
