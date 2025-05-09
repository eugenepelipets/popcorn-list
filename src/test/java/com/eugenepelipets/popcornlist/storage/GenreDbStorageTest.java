package com.eugenepelipets.popcornlist.storage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.eugenepelipets.popcornlist.model.Genre;
import com.eugenepelipets.popcornlist.storage.movie.GenreDbStorage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenreDbStorageTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private GenreDbStorage genreDbStorage;

    @Test
    void getGenreById_ShouldReturnGenre() {
        SqlRowSet rowSet = mock(SqlRowSet.class);
        when(rowSet.next()).thenReturn(true);
        when(rowSet.getInt("id")).thenReturn(1);
        when(rowSet.getString("genre_name")).thenReturn("Action");
        when(jdbcTemplate.queryForRowSet(anyString(), anyInt())).thenReturn(rowSet);
        
        Genre genre = genreDbStorage.getGenreById(1);
        
        assertEquals(1, genre.getId());
        assertEquals("Action", genre.getName());
    }
}