package com.eugenepelipets.popcornlist.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.eugenepelipets.popcornlist.exception.NotFoundException;
import com.eugenepelipets.popcornlist.model.Genre;
import com.eugenepelipets.popcornlist.service.GenreService;

import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenreControllerTest {

    @Mock
    private GenreService genreService;

    @InjectMocks
    private GenreController genreController;

    private Genre testGenre;

    @BeforeEach
    void setUp() {
        testGenre = Genre.builder()
                .id(1)
                .name("Action")
                .build();
    }

    @SuppressWarnings("null")
    @Test
    void getAllGenres_ShouldReturnGenres() {
        when(genreService.findAll()).thenReturn(Collections.singletonList(testGenre));

        ResponseEntity<Collection<Genre>> response = genreController.getAllGenres();

        assertEquals(1, response.getBody().size());
        assertEquals(testGenre, response.getBody().iterator().next());
    }

    @Test
    void getGenreById_ShouldReturnGenre() {
        when(genreService.getGenreById(1)).thenReturn(testGenre);

        ResponseEntity<Genre> response = genreController.getGenreById(1);

        assertEquals(testGenre, response.getBody());
    }

    @Test
    void getGenreById_ShouldThrowNotFoundException_WhenGenreNotExists() {
        when(genreService.getGenreById(anyInt())).thenThrow(new NotFoundException("Not found"));

        assertThrows(NotFoundException.class, () -> genreController.getGenreById(1));
    }
}