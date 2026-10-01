package com.passArena.movie_service.controller;

import com.passArena.movie_service.dto.CreateMovieRequest;
import com.passArena.movie_service.dto.MovieResponse;
import com.passArena.movie_service.dto.UpdateMovieRequest;
import com.passArena.movie_service.entity.MovieStatus;
import com.passArena.movie_service.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovieResponse createMovie(@Valid @RequestBody CreateMovieRequest createMovieRequest)
    {
        return movieService.createMovie(createMovieRequest);
    }

    @GetMapping("/{movieId}")
    public MovieResponse getMovie(@PathVariable UUID movieId)
    {
        return movieService.getMovieById(movieId);
    }

    @GetMapping("/status/{status}")
    public List<MovieResponse> getMoviesByStatus(@PathVariable MovieStatus status)
    {
        return movieService.getMoviesByStatus(status);
    }

    @GetMapping("/search")
    public List<MovieResponse> searchMovies(@RequestParam String title)
    {
        return movieService.searchMovies(title);
    }

    @PutMapping("{movieId}")
    public MovieResponse updateMovie(@PathVariable UUID movieId, @Valid @RequestBody UpdateMovieRequest request)
    {
        return movieService.updateMovie(movieId, request);
    }

    @DeleteMapping("{movieId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMovie(@PathVariable UUID movieId)
    {
        movieService.deleteMovie(movieId);
    }

}
