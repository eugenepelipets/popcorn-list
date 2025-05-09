package com.eugenepelipets.popcornlist.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.eugenepelipets.popcornlist.model.Movie;
import com.eugenepelipets.popcornlist.service.MovieService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieControllerTest {

    @Mock
    private MovieService movieService;

    @InjectMocks
    private MovieController movieController;

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
    void createMovie_ShouldReturnCreatedMovie() {
        when(movieService.create(any(Movie.class))).thenReturn(testMovie);

        ResponseEntity<Movie> response = movieController.createMovie(testMovie);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testMovie, response.getBody());
        verify(movieService, times(1)).create(testMovie);
    }

    @Test
    void updateMovie_ShouldReturnUpdatedMovie() {
        when(movieService.update(any(Movie.class))).thenReturn(testMovie);

        ResponseEntity<Movie> response = movieController.updateMovie(testMovie);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testMovie, response.getBody());
        verify(movieService, times(1)).update(testMovie);
    }

    @SuppressWarnings("null")
    @Test
    void getAllMovies_ShouldReturnAllMovies() {
        List<Movie> movies = Collections.singletonList(testMovie);
        when(movieService.findAll()).thenReturn(movies);

        ResponseEntity<Collection<Movie>> response = movieController.getAllMovies();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(testMovie, response.getBody().iterator().next());
        verify(movieService, times(1)).findAll();
    }

    @Test
    void getMovieById_ShouldReturnMovie() {
        when(movieService.getMovieById(1)).thenReturn(testMovie);

        ResponseEntity<Movie> response = movieController.getMovieById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testMovie, response.getBody());
        verify(movieService, times(1)).getMovieById(1);
    }

    @Test
    void addLike_ShouldCallService() {
        doNothing().when(movieService).addLike(1, 1);

        ResponseEntity<Void> response = movieController.addLike(1, 1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(movieService, times(1)).addLike(1, 1);
    }

    @SuppressWarnings("null")
    @Test
    void getPopularMovies_ShouldReturnPopularMovies() {
        List<Movie> movies = Collections.singletonList(testMovie);
        when(movieService.getPopularMovies(10)).thenReturn(movies);

        ResponseEntity<Collection<Movie>> response = movieController.getPopularMovies(10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(movieService, times(1)).getPopularMovies(10);
    }
}