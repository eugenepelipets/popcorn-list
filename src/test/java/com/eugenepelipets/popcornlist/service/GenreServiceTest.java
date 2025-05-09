package com.eugenepelipets.popcornlist.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.eugenepelipets.popcornlist.model.Genre;
import com.eugenepelipets.popcornlist.storage.movie.GenreDbStorage;

@ExtendWith(MockitoExtension.class)
class GenreServiceTest {

    @Mock
    private GenreDbStorage genreDbStorage;

    @InjectMocks
    private GenreService genreService;

    @Test
    void findAll_ShouldReturnAllGenres() {
        List<Genre> expected = List.of(new Genre(1, "Action"));
        
        when(genreDbStorage.findAll()).thenReturn(expected);
        
        Collection<Genre> result = genreService.findAll();
        
        assertEquals(expected, result);
    }
}