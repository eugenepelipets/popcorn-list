package com.eugenepelipets.popcornlist.controller;

import com.eugenepelipets.popcornlist.dto.GenreDto;
import com.eugenepelipets.popcornlist.mapper.GenreMapper;
import com.eugenepelipets.popcornlist.model.Genre;
import com.eugenepelipets.popcornlist.service.GenreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import com.eugenepelipets.popcornlist.exception.NotFoundException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenreControllerTest {

    @Mock
    private GenreService genreService;

    @Mock
    private GenreMapper genreMapper;

    @InjectMocks
    private GenreController genreController;

    private Genre testGenre;
    private GenreDto testDto;

    @BeforeEach
    void setUp() {
        testGenre = Genre.builder()
                .id(1)
                .name("Action")
                .build();

        testDto = new GenreDto();
        testDto.setId(1);
        testDto.setName("Action");
    }

    @Test
    void getAllGenres_ShouldReturnGenres() {
        when(genreService.findAll()).thenReturn(Collections.singletonList(testGenre));
        when(genreMapper.toDto(testGenre)).thenReturn(testDto);

        ResponseEntity<List<GenreDto>> response = genreController.getAllGenres();

        assertEquals(1, response.getBody().size());
        assertEquals(testDto, response.getBody().get(0));
        verify(genreService, times(1)).findAll();
        verify(genreMapper, times(1)).toDto(testGenre);
    }

    @Test
    void getGenreById_ShouldReturnGenre() {
        when(genreService.getGenreById(1)).thenReturn(testGenre);
        when(genreMapper.toDto(testGenre)).thenReturn(testDto);

        ResponseEntity<GenreDto> response = genreController.getGenreById(1);

        assertEquals(testDto, response.getBody());
        verify(genreService, times(1)).getGenreById(1);
        verify(genreMapper, times(1)).toDto(testGenre);
    }

    @Test
    void getGenreById_ShouldThrowNotFoundException_WhenGenreNotExists() {
        when(genreService.getGenreById(anyInt())).thenThrow(new NotFoundException("Not found"));

        assertThrows(NotFoundException.class, () -> genreController.getGenreById(1));
        verify(genreService, times(1)).getGenreById(1);
        verifyNoInteractions(genreMapper);
    }
}