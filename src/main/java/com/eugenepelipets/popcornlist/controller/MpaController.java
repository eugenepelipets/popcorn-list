package com.eugenepelipets.popcornlist.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eugenepelipets.popcornlist.model.Mpa;
import com.eugenepelipets.popcornlist.service.MpaService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {
    private final MpaService mpaService;

    @GetMapping
    public ResponseEntity<Collection<Mpa>> getAllMpaRatings() {
        log.info("GET /mpa - Retrieving all MPA ratings");
        return ResponseEntity.ok(mpaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mpa> getMpaRatingById(@PathVariable int id) {
        log.info("GET /mpa/{} - Retrieving MPA rating by ID", id);
        return ResponseEntity.ok(mpaService.getMpaById(id));
    }
}