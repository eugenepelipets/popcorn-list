package com.eugenepelipets.popcornlist.storage.movie;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.eugenepelipets.popcornlist.exception.NotFoundException;
import com.eugenepelipets.popcornlist.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Slf4j
@Component
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Mpa> findAll() {
        return jdbcTemplate.query("SELECT * FROM mpa", this::mapRowToMpa);
    }

    @Override
    public Mpa getMpaById(int id) {
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT * FROM mpa WHERE id = ?", id);
        if (mpaRows.next()) {
            Mpa mpa = new Mpa();
            mpa.setId(mpaRows.getInt("id"));
            mpa.setName(mpaRows.getString("mpa_name"));
            return mpa;
        } else {
            throw new NotFoundException("Рейтинг MPA не найден");
        }
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("mpa_name"))
                .build();
    }

}