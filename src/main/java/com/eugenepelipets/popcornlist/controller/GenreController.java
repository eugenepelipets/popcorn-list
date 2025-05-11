package com.eugenepelipets.popcornlist.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.eugenepelipets.popcornlist.dto.GenreDto;
import com.eugenepelipets.popcornlist.mapper.GenreMapper;
import com.eugenepelipets.popcornlist.service.GenreService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;
    private final GenreMapper genreMapper;

    @GetMapping
    public ResponseEntity<List<GenreDto>> getAllGenres() {
        log.info("GET /genres - Retrieving all genres");
        List<GenreDto> dtos = genreService.findAll()
            .stream()
            .map(genreMapper::toDto)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreDto> getGenreById(@PathVariable int id) {
        log.info("GET /genres/{} - Retrieving genre by ID", id);
        GenreDto dto = genreMapper.toDto(genreService.getGenreById(id));
        return ResponseEntity.ok(dto);
    }
}