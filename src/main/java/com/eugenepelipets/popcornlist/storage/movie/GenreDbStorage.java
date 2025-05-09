package com.eugenepelipets.popcornlist.storage.movie;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.eugenepelipets.popcornlist.exception.NotFoundException;
import com.eugenepelipets.popcornlist.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Genre> findAll() {
        return jdbcTemplate.query("SELECT * FROM genres", this::mapRowToGenre);
    }

    @Override
    public Genre getGenreById(int id) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT * FROM genres WHERE id = ?", id);
        if (genreRows.next()) {
            Genre genre = new Genre();
            genre.setId(genreRows.getInt("id"));
            genre.setName(genreRows.getString("genre_name"));
            return genre;
        } else {
            throw new NotFoundException("Genre not found");
        }
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("genre_name"))
                .build();
    }

    public List<Genre> getGenre(int id) {
        String sqlQuery = "SELECT * FROM genres AS g LEFT OUTER JOIN movies_genres AS fg " +
                "ON g.id = fg.genre_id WHERE fg.movie_id = ? GROUP BY fg.genre_id ORDER BY fg.movie_id";
        return jdbcTemplate.query(sqlQuery, this::composeGenre, id);
    }

    private Genre composeGenre(ResultSet resultSet, int rowNum) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("genre_name");
        return new Genre(id, name);
    }

}