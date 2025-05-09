package com.eugenepelipets.popcornlist.storage;

import com.eugenepelipets.popcornlist.exception.DataOperationException;
import com.eugenepelipets.popcornlist.exception.NotFoundException;
import com.eugenepelipets.popcornlist.exception.ValidationException;
import com.eugenepelipets.popcornlist.model.Mpa;
import com.eugenepelipets.popcornlist.model.Movie;
import com.eugenepelipets.popcornlist.storage.movie.MovieDbStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieDbStorageTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private SimpleJdbcInsert simpleJdbcInsert;

    @InjectMocks
    private MovieDbStorage movieDbStorage;

    private Movie validMovie;

    @BeforeEach
    void setUp() {
        movieDbStorage.setSimpleJdbcInsert(simpleJdbcInsert);

        validMovie = new Movie();
        validMovie.setName("Test Movie");
        validMovie.setDescription("Test Description");
        validMovie.setReleaseDate(LocalDate.of(2000, 1, 1));
        validMovie.setDuration(120);
        validMovie.setMpa(new Mpa(1, "PG-13"));
    }

    @SuppressWarnings("unchecked")
    @Test
    void createMovie_ShouldReturnCreatedMovie() {
        when(simpleJdbcInsert.executeAndReturnKey(any(Map.class))).thenReturn(1);

        Movie result = movieDbStorage.create(validMovie);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(simpleJdbcInsert, times(1)).executeAndReturnKey(any(Map.class));
        verifyNoMoreInteractions(jdbcTemplate);
    }

    @Test
    void createMovie_ShouldThrowValidationException_WhenInvalidData() {
        Movie movie = new Movie();
        assertThrows(ValidationException.class, () -> movieDbStorage.create(movie));
    }

    @Test
    void createMovie_ShouldThrowDataOperationException_WhenDatabaseError() {
        when(simpleJdbcInsert.executeAndReturnKey(anyMap()))
                .thenThrow(new DataAccessException("DB failure") {});

        assertThrows(DataOperationException.class, () -> movieDbStorage.create(validMovie));
    }

    @Test
    void updateMovie_ShouldReturnUpdatedMovie() {
        Movie movie = validMovie;
        movie.setId(1);

        when(jdbcTemplate.update(
                eq("UPDATE movies SET movie_name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? WHERE id = ?"),
                eq(movie.getName()), eq(movie.getDescription()), eq(movie.getReleaseDate()), eq(movie.getDuration()), eq(movie.getMpa().getId()), eq(movie.getId())
        )).thenReturn(1);
        when(jdbcTemplate.update("DELETE FROM movies_genres WHERE movie_id = ?", movie.getId()))
                .thenReturn(1);

        Movie result = movieDbStorage.update(movie);

        assertNotNull(result);
        assertEquals(movie, result);
        verify(jdbcTemplate).update(
                "UPDATE movies SET movie_name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? WHERE id = ?",
                movie.getName(), movie.getDescription(), movie.getReleaseDate(), movie.getDuration(), movie.getMpa().getId(), movie.getId());
        verify(jdbcTemplate).update("DELETE FROM movies_genres WHERE movie_id = ?", movie.getId());
    }

    @Test
    void getMovieById_ShouldThrowNotFoundException_WhenMovieNotExists() {
        assertThrows(NotFoundException.class, () -> movieDbStorage.getMovieById(999));
    }
}
