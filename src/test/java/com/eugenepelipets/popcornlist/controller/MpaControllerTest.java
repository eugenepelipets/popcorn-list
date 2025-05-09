package com.eugenepelipets.popcornlist.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.eugenepelipets.popcornlist.model.Mpa;
import com.eugenepelipets.popcornlist.service.MpaService;

import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MpaControllerTest {

    @Mock
    private MpaService mpaService;

    @InjectMocks
    private MpaController mpaController;

    private Mpa testMpa;

    @BeforeEach
    void setUp() {
        testMpa = Mpa.builder()
                .id(1)
                .name("PG-13")
                .build();
    }

    @SuppressWarnings("null")
    @Test
    void getAllMpaRatings_ShouldReturnMpas() {
        when(mpaService.findAll()).thenReturn(Collections.singletonList(testMpa));
        
        ResponseEntity<Collection<Mpa>> response = mpaController.getAllMpaRatings();
        
        assertEquals(1, response.getBody().size());
        assertEquals(testMpa, response.getBody().iterator().next());
    }

    @Test
    void getMpaRatingById_ShouldReturnMpa() {
        when(mpaService.getMpaById(1)).thenReturn(testMpa);
        
        ResponseEntity<Mpa> response = mpaController.getMpaRatingById(1);
        
        assertEquals(testMpa, response.getBody());
    }
}