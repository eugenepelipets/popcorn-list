package com.eugenepelipets.popcornlist.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eugenepelipets.popcornlist.model.Genre;
import com.eugenepelipets.popcornlist.service.GenreService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    public ResponseEntity<Collection<Genre>> getAllGenres() {
        log.info("GET /genres - Retrieving all genres");
        return ResponseEntity.ok(genreService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Genre> getGenreById(@PathVariable int id) {
        log.info("GET /genres/{} - Retrieving genre by ID", id);
        return ResponseEntity.ok(genreService.getGenreById(id));
    }
}