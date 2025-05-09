package com.eugenepelipets.popcornlist.controller;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eugenepelipets.popcornlist.model.Movie;
import com.eugenepelipets.popcornlist.service.MovieService;

import jakarta.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;
    private static final Logger log = LoggerFactory.getLogger(MovieController.class);

    @PostMapping
    public ResponseEntity<Movie> createMovie(@Valid @RequestBody Movie movie) {
        log.info("POST /movies - Creating new movie: {}", movie);
        Movie createdMovie = movieService.create(movie);
        return ResponseEntity.ok(createdMovie);
    }

    @PutMapping
    public ResponseEntity<Movie> updateMovie(@Valid @RequestBody Movie movie) {
        log.info("PUT /movies - Updating movie with ID: {}", movie.getId());
        Movie updatedMovie = movieService.update(movie);
        return ResponseEntity.ok(updatedMovie);
    }

    @GetMapping
    public ResponseEntity<Collection<Movie>> getAllMovies() {
        log.info("GET /movies - Retrieving all movies");
        return ResponseEntity.ok(movieService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable int id) {
        log.info("GET /movies/{} - Retrieving movie by ID", id);
        return ResponseEntity.ok(movieService.getMovieById(id));
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<Void> addLike(
            @PathVariable int id,
            @PathVariable int userId) {
        log.info("PUT /movies/{}/like/{} - Adding like", id, userId);
        movieService.addLike(id, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<Void> removeLike(
            @PathVariable int id,
            @PathVariable int userId) {
        log.info("DELETE /movies/{}/like/{} - Removing like", id, userId);
        movieService.removeLike(id, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/popular")
    public ResponseEntity<Collection<Movie>> getPopularMovies(
            @RequestParam(defaultValue = "10", required = false) Integer count) {
        log.info("GET /movies/popular?count={} - Retrieving popular movies", count);
        return ResponseEntity.ok(movieService.getPopularMovies(count));
    }
}