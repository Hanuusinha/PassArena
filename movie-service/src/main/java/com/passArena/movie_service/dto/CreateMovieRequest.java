package com.passArena.movie_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateMovieRequest {

    @NotBlank
    private String title;

    private String description;

    @NotNull
    @Positive
    private Integer durationInMinutes;

    @NotBlank
    private String language;

    @NotBlank
    private String genre;

    private LocalDate releaseDate;

    private String posterUrl;
}
