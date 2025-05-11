package com.eugenepelipets.popcornlist.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.eugenepelipets.popcornlist.dto.MovieRequestDto;
import com.eugenepelipets.popcornlist.dto.MovieResponseDto;
import com.eugenepelipets.popcornlist.mapper.MovieMapper;
import com.eugenepelipets.popcornlist.model.Movie;
import com.eugenepelipets.popcornlist.service.MovieService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;
    private final MovieMapper movieMapper;

    @PostMapping
    public ResponseEntity<MovieResponseDto> createMovie(@Valid @RequestBody MovieRequestDto dto) {
        Movie movie = movieMapper.toEntity(dto);
        Movie created = movieService.create(movie);
        MovieResponseDto response = movieMapper.toDto(created);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<MovieResponseDto> updateMovie(@Valid @RequestBody MovieRequestDto dto) {
        Movie movie = movieMapper.toEntity(dto);
        Movie updated = movieService.update(movie);
        MovieResponseDto response = movieMapper.toDto(updated);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<MovieResponseDto>> getAllMovies() {
        List<MovieResponseDto> dtos = movieService.findAll()
                .stream()
                .map(movieMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieResponseDto> getMovieById(
            @PathVariable @Positive(message = "ID must be positive") int id) {
        Movie movie = movieService.getMovieById(id);
        return ResponseEntity.ok(movieMapper.toDto(movie));
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<Void> addLike(@PathVariable int id, @PathVariable int userId) {
        movieService.addLike(id, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<Void> removeLike(@PathVariable int id, @PathVariable int userId) {
        movieService.removeLike(id, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/popular")
    public ResponseEntity<List<MovieResponseDto>> getPopularMovies(
            @RequestParam(defaultValue = "10") Integer count) {
        List<MovieResponseDto> dtos = movieService.getPopularMovies(count)
                .stream()
                .map(movieMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}