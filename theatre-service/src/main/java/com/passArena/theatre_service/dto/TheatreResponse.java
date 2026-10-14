package com.passArena.theatre_service.dto;

import com.passArena.theatre_service.entity.TheatreStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TheatreResponse {

    private String name;
    private String address;
    private String city;
    private TheatreStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
