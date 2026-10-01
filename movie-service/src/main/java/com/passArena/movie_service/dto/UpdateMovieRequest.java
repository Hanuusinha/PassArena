package com.passArena.movie_service.dto;

import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateMovieRequest {

    private String title;
    private String description;

    @Positive
    private Integer durationInMinutes;

    private String language;
    private String genre;
    private LocalDate releaseDate;
    private String posterUrl;
}
