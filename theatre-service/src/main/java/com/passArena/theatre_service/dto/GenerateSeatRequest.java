package com.passArena.theatre_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class GenerateSeatRequest {

    @NotEmpty
    @Valid
    private List<SeatRowRequest> rows;
}
