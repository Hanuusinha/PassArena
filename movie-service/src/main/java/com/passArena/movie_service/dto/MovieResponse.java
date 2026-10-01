package com.passArena.movie_service.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class MovieResponse {

    private UUID id;
    private String title;
    private String description;
    private Integer durationInMinutes;
    private String language;
    private String genre;
    private LocalDate releaseDate;
    private String posterUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
