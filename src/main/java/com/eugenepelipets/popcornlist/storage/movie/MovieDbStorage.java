package com.eugenepelipets.popcornlist.storage.movie;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import com.eugenepelipets.popcornlist.exception.*;
import com.eugenepelipets.popcornlist.model.Movie;
import com.eugenepelipets.popcornlist.model.Mpa;
import com.eugenepelipets.popcornlist.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Array;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class MovieDbStorage implements MovieStorage {
    private static final String UPDATE_MOVIE = "UPDATE movies SET movie_name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? WHERE id = ?";
    private static final String INSERT_GENRE = "INSERT INTO movies_genres (movie_id, genre_id) VALUES (?, ?)";
    private static final String DELETE_GENRES = "DELETE FROM movies_genres WHERE movie_id = ?";
    private static final String SELECT_POPULAR_MOVIES = """
            SELECT DISTINCT
                m.id as movie_id, m.movie_name, m.description,
                m.release_date, m.duration,
                mpa.id as mpa_id, mpa.mpa_name,
                g.id as genre_id, g.genre_name,
                COUNT(ml.user_id) AS likes_count
            FROM movies m
            JOIN mpa ON m.mpa_id = mpa.id
            LEFT JOIN movies_genres mg ON m.id = mg.movie_id
            LEFT JOIN genres g ON mg.genre_id = g.id
            LEFT JOIN movies_likes ml ON m.id = ml.movie_id
            GROUP BY m.id, mpa.id, g.id
            ORDER BY likes_count DESC
            LIMIT ?""";
    private static final String SELECT_ALL_MOVIES_WITH_GENRES = """
            SELECT m.id, m.movie_name, m.description,
                   m.release_date, m.duration,
                   mpa.id AS mpa_id, mpa.mpa_name,
                   ARRAY_AGG(g.id) FILTER (WHERE g.id IS NOT NULL) AS genre_ids,
                   ARRAY_AGG(g.genre_name) FILTER (WHERE g.genre_name IS NOT NULL) AS genre_names
            FROM movies m
            JOIN mpa ON m.mpa_id = mpa.id
            LEFT JOIN movies_genres mg ON m.id = mg.movie_id
            LEFT JOIN genres g ON mg.genre_id = g.id
            GROUP BY m.id, mpa.id""";

    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    public void setSimpleJdbcInsert(SimpleJdbcInsert simpleJdbcInsert) {
        this.simpleJdbcInsert = simpleJdbcInsert;
    }

    @Override
    public Movie create(Movie movie) {
        validateMovie(movie);

        SimpleJdbcInsert insert = this.simpleJdbcInsert;
        if (insert == null) {
            insert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("movies")
                    .usingGeneratedKeyColumns("id");
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("movie_name", movie.getName());
        parameters.put("description", movie.getDescription());
        parameters.put("release_date", movie.getReleaseDate());
        parameters.put("duration", movie.getDuration());
        parameters.put("mpa_id", movie.getMpa().getId());

        try {
            Number id = insert.executeAndReturnKey(parameters);
            movie.setId(id.intValue());

            if (movie.getGenres() != null && !movie.getGenres().isEmpty()) {
                Set<Genre> uniqueGenres = new HashSet<>(movie.getGenres());
                for (Genre genre : uniqueGenres) {
                    jdbcTemplate.update(INSERT_GENRE, movie.getId(), genre.getId());
                }
                movie.setGenres(new ArrayList<>(uniqueGenres));
            }

            log.info("Created new movie with ID: {}", movie.getId());
            return movie;
        } catch (DataAccessException e) {
            log.error("Failed to create movie", e);
            throw new DataOperationException("Failed to create movie", e);
        }
    }

    @Override
    public Movie update(Movie movie) {
        validateMovie(movie);

        try {
            int updated = jdbcTemplate.update(UPDATE_MOVIE,
                    movie.getName(),
                    movie.getDescription(),
                    movie.getReleaseDate(),
                    movie.getDuration(),
                    movie.getMpa().getId(),
                    movie.getId());

            if (updated == 0) {
                throw new NotFoundException("Movie with ID " + movie.getId() + " not found");
            }

            jdbcTemplate.update(DELETE_GENRES, movie.getId());

            if (movie.getGenres() != null && !movie.getGenres().isEmpty()) {
                Set<Genre> uniqueGenres = new HashSet<>(movie.getGenres());
                for (Genre genre : uniqueGenres) {
                    jdbcTemplate.update(INSERT_GENRE, movie.getId(), genre.getId());
                }
                movie.setGenres(new ArrayList<>(uniqueGenres));
            }

            log.info("Updated movie with ID: {}", movie.getId());
            return movie;
        } catch (DataAccessException e) {
            log.error("Failed to update movie with ID: {}", movie.getId(), e);
            throw new DataOperationException("Failed to update movie", e);
        }
    }

    @Override
    public Collection<Movie> findAll() {
        return jdbcTemplate.query(SELECT_ALL_MOVIES_WITH_GENRES, this::mapRowToMovie);
    }

    @Override
    public Movie getMovieById(int id) {
        try {
            String query = SELECT_ALL_MOVIES_WITH_GENRES + " WHERE m.id = ?";

            Map<Integer, Movie> movieMap = new HashMap<>();

            jdbcTemplate.query(query, rs -> {
                int movieId = rs.getInt("movie_id");

                Movie movie = movieMap.computeIfAbsent(movieId, k -> {
                    try {
                        return Movie.builder()
                                .id(movieId)
                                .name(rs.getString("movie_name"))
                                .description(rs.getString("description"))
                                .releaseDate(
                                        rs.getDate("release_date") != null ? rs.getDate("release_date").toLocalDate()
                                                : null)
                                .duration(rs.getInt("duration"))
                                .mpa(new Mpa(
                                        rs.getInt("mpa_id"),
                                        rs.getString("mpa_name")))
                                .genres(new ArrayList<>())
                                .build();
                    } catch (SQLException e) {
                        throw new DataOperationException("Error mapping movie", e);
                    }
                });

                int genreId = rs.getInt("genre_id");
                if (!rs.wasNull()) {
                    movie.getGenres().add(new Genre(
                            genreId,
                            rs.getString("genre_name")));
                }
            }, id);

            return Optional.ofNullable(movieMap.get(id))
                    .orElseThrow(() -> new NotFoundException("Movie with ID " + id + " not found"));

        } catch (DataAccessException e) {
            log.error("Failed to retrieve movie with ID: {}", id, e);
            throw new DataOperationException("Failed to retrieve movie", e);
        }
    }

    public Collection<Movie> getPopularMovies(int count) {
        try {
            Map<Integer, Movie> movieMap = new LinkedHashMap<>();

            jdbcTemplate.query(SELECT_POPULAR_MOVIES, rs -> {
                int movieId = rs.getInt("movie_id");

                Movie movie = movieMap.computeIfAbsent(movieId, id -> {
                    try {
                        return Movie.builder()
                                .id(id)
                                .name(rs.getString("movie_name"))
                                .description(rs.getString("description"))
                                .releaseDate(rs.getDate("release_date").toLocalDate())
                                .duration(rs.getInt("duration"))
                                .mpa(new Mpa(
                                        rs.getInt("mpa_id"),
                                        rs.getString("mpa_name")))
                                .genres(new ArrayList<>())
                                .build();
                    } catch (SQLException e) {
                        throw new DataOperationException("Error mapping movie", e);
                    }
                });

                int genreId = rs.getInt("genre_id");
                if (!rs.wasNull()) {
                    movie.getGenres().add(new Genre(
                            genreId,
                            rs.getString("genre_name")));
                }
            }, count);

            return new ArrayList<>(movieMap.values());
        } catch (DataOperationException e) {
            log.error("Failed to retrieve popular movies", e);
            throw e;
        }
    }

    private void validateMovie(Movie movie) {
        if (movie.getName() == null || movie.getName().isBlank()) {
            throw new ValidationException("Movie name cannot be empty");
        }
        if (movie.getDescription() != null && movie.getDescription().length() > 200) {
            throw new ValidationException("Description cannot be longer than 200 characters");
        }
        if (movie.getReleaseDate() == null || movie.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Release date must be after 28 December 1895");
        }
        if (movie.getDuration() <= 0) {
            throw new ValidationException("Duration must be positive");
        }
        if (movie.getMpa() == null) {
            throw new ValidationException("MPA rating cannot be null");
        }
    }

    private Movie mapRowToMovie(ResultSet rs, int rowNum) throws SQLException {
        Array genreIdsArray = rs.getArray("genre_ids");
        Array genreNamesArray = rs.getArray("genre_names");

        List<Integer> genreIds = new ArrayList<>();
        List<String> genreNames = new ArrayList<>();

        if (genreIdsArray != null) {
            Object[] ids = (Object[]) genreIdsArray.getArray();
            for (Object id : ids) {
                if (id != null) {
                    genreIds.add(((Number) id).intValue());
                }
            }
        }

        if (genreNamesArray != null) {
            Object[] names = (Object[]) genreNamesArray.getArray();
            for (Object name : names) {
                if (name != null) {
                    genreNames.add((String) name);
                }
            }
        }

        List<Genre> genres = new ArrayList<>();
        for (int i = 0; i < genreIds.size(); i++) {
            genres.add(new Genre(genreIds.get(i), genreNames.get(i)));
        }

        return Movie.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("movie_name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(new Mpa(rs.getInt("mpa_id"), rs.getString("mpa_name")))
                .genres(genres)
                .build();
    }
}