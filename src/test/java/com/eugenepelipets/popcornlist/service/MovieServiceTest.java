package com.eugenepelipets.popcornlist.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import com.eugenepelipets.popcornlist.exception.ValidationException;
import com.eugenepelipets.popcornlist.model.Genre;
import com.eugenepelipets.popcornlist.model.Movie;
import com.eugenepelipets.popcornlist.storage.movie.MovieDbStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieDbStorage movieDbStorage;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private MovieService movieService;

    private Movie testMovie;

    @BeforeEach
    void setUp() {
        testMovie = Movie.builder()
                .id(1)
                .name("Test Movie")
                .description("Test Description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(120)
                .build();
    }

    @Test
    void createMovie_ShouldReturnMovie() {
        when(movieDbStorage.create(any(Movie.class))).thenReturn(testMovie);

        Movie result = movieService.create(testMovie);

        assertEquals(testMovie, result);
        verify(movieDbStorage, times(1)).create(testMovie);
    }

    @Test
    void updateMovie_ShouldReturnUpdatedMovie() {
        when(movieDbStorage.update(any(Movie.class))).thenReturn(testMovie);

        Movie result = movieService.update(testMovie);

        assertEquals(testMovie, result);
        verify(movieDbStorage, times(1)).update(testMovie);
    }

    @Test
    void addLike_ShouldCallJdbcTemplate() {
        when(jdbcTemplate.update(anyString(), anyInt(), anyInt())).thenReturn(1);

        movieService.addLike(1, 1);

        verify(jdbcTemplate, times(1)).update(anyString(), eq(1), eq(1));
    }

    @Test
    void getPopularMovies_ShouldReturnMovies() {
        List<Movie> movies = Collections.singletonList(testMovie);
        when(movieDbStorage.getPopularMovies(anyInt())).thenReturn(movies);

        Collection<Movie> result = movieService.getPopularMovies(10);

        assertEquals(1, result.size());
        assertEquals(testMovie, result.iterator().next());
        verify(movieDbStorage, times(1)).getPopularMovies(10);
    }

    @Test
    void removeLike_ShouldCallJdbcTemplate() {
        when(jdbcTemplate.update(anyString(), anyInt(), anyInt())).thenReturn(1);

        movieService.removeLike(1, 1);

        verify(jdbcTemplate, times(1)).update(anyString(), eq(1), eq(1));
    }

    @Test
    void validateLikeOperation_ShouldThrowForInvalidIds() {
        assertThrows(ValidationException.class, () -> movieService.addLike(0, 1));
        assertThrows(ValidationException.class, () -> movieService.addLike(1, -1));
    }

    @Test
    void findAll_ShouldReturnMovies() {
        when(movieDbStorage.findAll()).thenReturn(Collections.singletonList(testMovie));

        Collection<Movie> result = movieService.findAll();

        assertEquals(1, result.size());
        assertEquals(testMovie, result.iterator().next());
    }

    @Test
    void getMovieById_ShouldReturnMovie() {
        when(movieDbStorage.getMovieById(1)).thenReturn(testMovie);

        Movie result = movieService.getMovieById(1);

        assertEquals(testMovie, result);
    }

    @Test
    void updateMovieGenres_ShouldClearExistingGenres() {
        movieService.updateMovieGenres(1, List.of());
        verify(jdbcTemplate).update("DELETE FROM movies_genres WHERE movie_id = ?", 1);
    }

    @Test
    void updateMovieGenres_ShouldAddNewGenres() {
        Genre genre = new Genre(1, "Action");
        movieService.updateMovieGenres(1, List.of(genre));

        verify(jdbcTemplate).batchUpdate(
                eq("INSERT INTO movies_genres (movie_id, genre_id) VALUES (?, ?)"),
                anyList());
    }
}