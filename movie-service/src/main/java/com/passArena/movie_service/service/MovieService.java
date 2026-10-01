package com.passArena.movie_service.service;

import com.passArena.movie_service.dto.CreateMovieRequest;
import com.passArena.movie_service.dto.MovieResponse;
import com.passArena.movie_service.dto.UpdateMovieRequest;
import com.passArena.movie_service.entity.Movie;
import com.passArena.movie_service.entity.MovieStatus;
import com.passArena.movie_service.exception.MovieNotFoundException;
import com.passArena.movie_service.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieResponse createMovie(CreateMovieRequest createMovieRequest) {
        Movie movie = Movie.builder()
                .title(createMovieRequest.getTitle())
                .description(createMovieRequest.getDescription())
                .durationInMinutes(createMovieRequest.getDurationInMinutes())
                .language(createMovieRequest.getLanguage())
                .genre(createMovieRequest.getGenre())
                .releaseDate(createMovieRequest.getReleaseDate())
                .posterUrl(createMovieRequest.getPosterUrl())
                .status(MovieStatus.UPCOMING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Movie savedMovie = movieRepository.save(movie);
        return mapToResponse(savedMovie);
    }

    public MovieResponse getMovieById(UUID movieId)
    {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found with id: " + movieId));
        return mapToResponse(movie);
    }

    public List<MovieResponse> getAllMovies() {
        return movieRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<MovieResponse> getMoviesByStatus(MovieStatus status)
    {
        return movieRepository.findByStatus(status)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<MovieResponse> searchMovies(String title)
    {
        return movieRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public MovieResponse updateMovie(UUID movieId, UpdateMovieRequest request)
    {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found with id: " + movieId));

        if(request.getTitle() != null) {
            movie.setTitle(request.getTitle());
        }
        if(request.getDescription() != null) {
            movie.setDescription(request.getDescription());
        }

        if(request.getDurationInMinutes() != null) {
            movie.setDurationInMinutes(request.getDurationInMinutes());
        }
        if(request.getLanguage() != null) {
            movie.setLanguage(request.getLanguage());
        }

        if(request.getGenre() != null) {
            movie.setGenre(request.getGenre());
        }

        if(request.getReleaseDate() != null) {
            movie.setReleaseDate(request.getReleaseDate());
        }

        if(request.getPosterUrl() != null) {
            movie.setPosterUrl(request.getPosterUrl());
        }

        movie.setUpdatedAt(LocalDateTime.now());

        return mapToResponse(movieRepository.save(movie));


    }

    public void deleteMovie(UUID movieId)
    {
        if(!movieRepository.existsById(movieId))
        {
            throw new MovieNotFoundException("Movie not found with id: " + movieId);
        }

        movieRepository.deleteById(movieId);
    }

    private MovieResponse mapToResponse(Movie movie)
    {
        return MovieResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .description(movie.getDescription())
                .durationInMinutes(movie.getDurationInMinutes())
                .language(movie.getLanguage())
                .genre(movie.getGenre())
                .releaseDate(movie.getReleaseDate())
                .posterUrl(movie.getPosterUrl())
                .createdAt(movie.getCreatedAt())
                .updatedAt(movie.getUpdatedAt())
                .build();
    }
}
