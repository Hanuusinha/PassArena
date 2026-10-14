package com.passArena.theatre_service.dto;

import com.passArena.theatre_service.entity.SeatType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class SeatResponse {

    private UUID id;
    private String seatNumber;
    private SeatType seatType;
    private String rowNumber;
    private BigDecimal seatPrice;
}
