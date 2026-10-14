package com.passArena.theatre_service.mapper;

import com.passArena.theatre_service.dto.SeatResponse;
import com.passArena.theatre_service.entity.Seat;
import org.springframework.stereotype.Component;

@Component
public class SeatMapper {

    public SeatResponse toResponse(Seat seat)
    {
        return SeatResponse.builder()
                .id(seat.getId())
                .seatNumber(seat.getSeatNumber())
                .seatType(seat.getSeatType())
                .rowNumber(seat.getRowNumber())
                .seatPrice(seat.getSeatPrice())
                .build();
    }
}
