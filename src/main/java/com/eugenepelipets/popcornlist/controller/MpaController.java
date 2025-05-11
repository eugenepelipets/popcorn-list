package com.eugenepelipets.popcornlist.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.eugenepelipets.popcornlist.dto.MpaDto;
import com.eugenepelipets.popcornlist.mapper.MpaMapper;
import com.eugenepelipets.popcornlist.service.MpaService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {
    private final MpaService mpaService;
    private final MpaMapper mpaMapper;

    @GetMapping
    public ResponseEntity<List<MpaDto>> getAllMpaRatings() {
        log.info("GET /mpa - Retrieving all MPA ratings");
        List<MpaDto> dtos = mpaService.findAll()
            .stream()
            .map(mpaMapper::toDto)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MpaDto> getMpaRatingById(@PathVariable int id) {
        log.info("GET /mpa/{} - Retrieving MPA rating by ID", id);
        MpaDto dto = mpaMapper.toDto(mpaService.getMpaById(id));
        return ResponseEntity.ok(dto);
    }
}