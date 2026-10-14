package com.passArena.theatre_service.mapper;

import com.passArena.theatre_service.dto.CreateTheatreRequest;
import com.passArena.theatre_service.dto.TheatreResponse;
import com.passArena.theatre_service.entity.Theatre;
import com.passArena.theatre_service.entity.TheatreStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TheatreMapper {

    public Theatre toEntity(CreateTheatreRequest request)
    {
        return Theatre.builder()
                .name(request.getName())
                .address(request.getAddress())
                .city(request.getCity())
                .status(TheatreStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public TheatreResponse toResponse(Theatre theatre)
    {
        return TheatreResponse.builder()
                .name(theatre.getName())
                .address(theatre.getAddress())
                .city(theatre.getCity())
                .status(theatre.getStatus())
                .createdAt(theatre.getCreatedAt())
                .updatedAt(theatre.getUpdatedAt())
                .build();
    }
}
