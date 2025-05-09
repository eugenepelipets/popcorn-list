package com.eugenepelipets.popcornlist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.eugenepelipets.popcornlist.exception.*;
import com.eugenepelipets.popcornlist.model.Genre;
import com.eugenepelipets.popcornlist.model.Movie;
import com.eugenepelipets.popcornlist.storage.movie.MovieDbStorage;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MovieService {

    private final JdbcTemplate jdbcTemplate;
    private final MovieDbStorage movieDbStorage;

    @Transactional
    public Movie create(Movie movie) {
        log.info("Creating movie: {}", movie.getName());
        try {
            Movie createdMovie = movieDbStorage.create(movie);
            updateMovieGenres(createdMovie.getId(), movie.getGenres());
            return createdMovie;
        } catch (DataAccessException e) {
            log.error("Movie creation failed", e);
            throw new DataOperationException("Failed to create movie", e);
        }
    }

    @Transactional
    public Movie update(Movie movie) {
        log.info("Updating movie ID: {}", movie.getId());
        try {
            Movie updatedMovie = movieDbStorage.update(movie);
            updateMovieGenres(updatedMovie.getId(), movie.getGenres());
            return updatedMovie;
        } catch (DataAccessException e) {
            log.error("Movie update failed", e);
            throw new DataOperationException("Failed to update movie", e);
        }
    }

    void updateMovieGenres(int movieId, List<Genre> genres) {
        jdbcTemplate.update("DELETE FROM movies_genres WHERE movie_id = ?", movieId);

        if (genres != null && !genres.isEmpty()) {
            Set<Integer> genreIds = genres.stream()
                    .filter(Objects::nonNull)
                    .map(Genre::getId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            jdbcTemplate.batchUpdate(
                    "INSERT INTO movies_genres (movie_id, genre_id) VALUES (?, ?)",
                    genreIds.stream()
                            .map(genreId -> new Object[] { movieId, genreId })
                            .collect(Collectors.toList()));
        }
    }

    public Collection<Movie> findAll() {
        return movieDbStorage.findAll();
    }

    public Movie getMovieById(int id) {
        return movieDbStorage.getMovieById(id);
    }

    @Transactional
    public void addLike(int movieId, int userId) {
        validateLikeOperation(movieId, userId);
        try {
            int updated = jdbcTemplate.update(
                    "INSERT INTO movies_likes (movie_id, user_id) VALUES (?, ?)",
                    movieId, userId);
            if (updated == 0) {
                throw new NotFoundException("Movie or user not found");
            }
        } catch (DataAccessException e) {
            throw new DataOperationException("Like operation failed", e);
        }
    }

    @Transactional
    public void removeLike(int movieId, int userId) {
        validateLikeOperation(movieId, userId);
        try {
            int updated = jdbcTemplate.update(
                    "DELETE FROM movies_likes WHERE movie_id = ? AND user_id = ?",
                    movieId, userId);
            if (updated == 0) {
                throw new NotFoundException("Like not found");
            }
        } catch (DataAccessException e) {
            throw new DataOperationException("Unlike operation failed", e);
        }
    }

    private void validateLikeOperation(int movieId, int userId) {
        if (movieId <= 0 || userId <= 0) {
            throw new ValidationException("Invalid movie or user ID");
        }
    }

    public Collection<Movie> getPopularMovies(int count) {
        return movieDbStorage.getPopularMovies(Math.max(count, 1));
    }
}