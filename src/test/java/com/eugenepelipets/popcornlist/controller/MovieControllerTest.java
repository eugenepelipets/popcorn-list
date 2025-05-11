package com.eugenepelipets.popcornlist.controller;

import com.eugenepelipets.popcornlist.dto.MovieRequestDto;
import com.eugenepelipets.popcornlist.dto.MovieResponseDto;
import com.eugenepelipets.popcornlist.dto.MpaDto;
import com.eugenepelipets.popcornlist.dto.GenreDto;
import com.eugenepelipets.popcornlist.mapper.MovieMapper;
import com.eugenepelipets.popcornlist.model.Movie;
import com.eugenepelipets.popcornlist.model.Mpa;
import com.eugenepelipets.popcornlist.model.Genre;
import com.eugenepelipets.popcornlist.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieControllerTest {

    @Mock
    private MovieService movieService;

    @Mock
    private MovieMapper movieMapper;

    @InjectMocks
    private MovieController movieController;

    private Movie testMovie;
    private MovieRequestDto requestDto;
    private MovieResponseDto responseDto;

    @BeforeEach
    void setUp() {
        testMovie = Movie.builder()
                .id(1)
                .name("Test Movie")
                .description("Test Description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(120)
                .mpa(new Mpa(1, "PG-13"))
                .genres(List.of(new Genre(1, "Action")))
                .build();

        requestDto = new MovieRequestDto();
        requestDto.setName(testMovie.getName());
        requestDto.setDescription(testMovie.getDescription());
        requestDto.setReleaseDate(testMovie.getReleaseDate());
        requestDto.setDuration(testMovie.getDuration());
        //requestDto.setMpaId(testMovie.getMpa().getId());
        requestDto.setGenreIds(List.of(testMovie.getGenres().get(0).getId()));

        GenreDto genreDto = new GenreDto();
        genreDto.setId(1);
        genreDto.setName("Action");
        MpaDto mpaDto = new MpaDto();
        mpaDto.setId(1);
        mpaDto.setName("PG-13");

        responseDto = new MovieResponseDto();
        responseDto.setId(testMovie.getId());
        responseDto.setName(testMovie.getName());
        responseDto.setDescription(testMovie.getDescription());
        responseDto.setReleaseDate(testMovie.getReleaseDate());
        responseDto.setDuration(testMovie.getDuration());
        responseDto.setMpa(mpaDto);
        responseDto.setGenres(List.of(genreDto));
    }

    @Test
    void createMovie_ShouldReturnCreatedMovie() {
        when(movieMapper.toEntity(requestDto)).thenReturn(testMovie);
        when(movieService.create(testMovie)).thenReturn(testMovie);
        when(movieMapper.toDto(testMovie)).thenReturn(responseDto);

        ResponseEntity<MovieResponseDto> response = movieController.createMovie(requestDto);

        assertEquals(200, response);
        assertEquals(responseDto, response.getBody());
        verify(movieMapper).toEntity(requestDto);
        verify(movieService).create(testMovie);
        verify(movieMapper).toDto(testMovie);
    }

    @Test
    void updateMovie_ShouldReturnUpdatedMovie() {
        when(movieMapper.toEntity(requestDto)).thenReturn(testMovie);
        when(movieService.update(testMovie)).thenReturn(testMovie);
        when(movieMapper.toDto(testMovie)).thenReturn(responseDto);

        ResponseEntity<MovieResponseDto> response = movieController.updateMovie(requestDto);

        assertEquals(responseDto, response.getBody());
        verify(movieMapper).toEntity(requestDto);
        verify(movieService).update(testMovie);
        verify(movieMapper).toDto(testMovie);
    }

    @Test
    void getAllMovies_ShouldReturnAllMovies() {
        when(movieService.findAll()).thenReturn(Collections.singletonList(testMovie));
        when(movieMapper.toDto(testMovie)).thenReturn(responseDto);

        ResponseEntity<List<MovieResponseDto>> response = movieController.getAllMovies();

        assertEquals(1, response.getBody().size());
        assertEquals(responseDto, response.getBody().get(0));
        verify(movieService).findAll();
        verify(movieMapper).toDto(testMovie);
    }

    @Test
    void getMovieById_ShouldReturnMovie() {
        when(movieService.getMovieById(1)).thenReturn(testMovie);
        when(movieMapper.toDto(testMovie)).thenReturn(responseDto);

        ResponseEntity<MovieResponseDto> response = movieController.getMovieById(1);

        assertEquals(responseDto, response.getBody());
        verify(movieService).getMovieById(1);
        verify(movieMapper).toDto(testMovie);
    }

    @Test
    void addLike_ShouldCallService() {
        doNothing().when(movieService).addLike(1, 1);

        ResponseEntity<Void> response = movieController.addLike(1, 1);

        assertEquals(200, response);
        verify(movieService).addLike(1, 1);
    }

    @Test
    void removeLike_ShouldCallService() {
        doNothing().when(movieService).removeLike(1, 1);

        ResponseEntity<Void> response = movieController.removeLike(1, 1);

        assertEquals(200, response);
        verify(movieService).removeLike(1, 1);
    }

    @Test
    void getPopularMovies_ShouldReturnPopularMovies() {
        when(movieService.getPopularMovies(5)).thenReturn(Collections.singletonList(testMovie));
        when(movieMapper.toDto(testMovie)).thenReturn(responseDto);

        ResponseEntity<List<MovieResponseDto>> response = movieController.getPopularMovies(5);

        assertEquals(1, response.getBody().size());
        assertEquals(responseDto, response.getBody().get(0));
        verify(movieService).getPopularMovies(5);
        verify(movieMapper).toDto(testMovie);
    }
}