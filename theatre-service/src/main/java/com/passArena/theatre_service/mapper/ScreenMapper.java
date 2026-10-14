package com.passArena.theatre_service.mapper;

import com.passArena.theatre_service.dto.ScreenResponse;
import com.passArena.theatre_service.entity.Screen;
import org.springframework.stereotype.Component;

@Component
public class ScreenMapper {

    public ScreenResponse toResponse(Screen screen)
    {
        return ScreenResponse.builder()
                .name(screen.getName())
                .screenType(screen.getScreenType())
                .totalSeats(screen.getTotalSeats())
                .createdAt(screen.getCreatedAt())
                .updatedAt(screen.getUpdatedAt())
                .build();
    }
}
