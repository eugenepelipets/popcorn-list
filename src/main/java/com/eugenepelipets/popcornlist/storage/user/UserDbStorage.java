package com.eugenepelipets.popcornlist.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.eugenepelipets.popcornlist.exception.*;
import com.eugenepelipets.popcornlist.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private static final String SELECT_USER_BY_ID = "SELECT * FROM users WHERE id = ?";
    private static final String UPDATE_USER = "UPDATE users SET email = ?, login = ?, user_name = ?, birthday = ? WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User create(User user) {
        validateUser(user);

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("email", user.getEmail());
        parameters.put("login", user.getLogin());
        // UserDbStorage.java
        parameters.put("user_name",
                user.getName() == null || user.getName().isBlank() ? user.getLogin() : user.getName());
        parameters.put("birthday", user.getBirthday());

        try {
            Number id = simpleJdbcInsert.executeAndReturnKey(parameters);
            user.setId(id.intValue());
            log.info("Created new user with ID: {}", user.getId());
            return user;
        } catch (DataAccessException e) {
            log.error("Failed to create user", e);
            throw new DataOperationException("Failed to create user", e);
        }
    }

    @Override
    public User update(User user) {
        validateUser(user);

        try {
            int updated = jdbcTemplate.update(UPDATE_USER,
                    user.getEmail(),
                    user.getLogin(),
                    user.getName(),
                    user.getBirthday(),
                    user.getId());

            if (updated == 0) {
                throw new NotFoundException("User with ID " + user.getId() + " not found");
            }

            log.info("Updated user with ID: {}", user.getId());
            return user;
        } catch (DataAccessException e) {
            log.error("Failed to update user with ID: {}", user.getId(), e);
            throw new DataOperationException("Failed to update user", e);
        }
    }

    @Override
    public User getUserById(int id) {
        try {
            SqlRowSet userRows = jdbcTemplate.queryForRowSet(SELECT_USER_BY_ID, id);
            if (userRows.next()) {
                return jdbcTemplate.queryForObject(SELECT_USER_BY_ID, this::mapRowToUser, id);
            } else {
                throw new NotFoundException("User with ID " + id + " not found");
            }
        } catch (DataAccessException e) {
            log.error("Failed to retrieve user with ID: {}", id, e);
            throw new DataOperationException("Failed to retrieve user", e);
        }
    }

    private User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getInt("id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("user_name"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();
    }

    private void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Email must not be empty and must contain @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            throw new ValidationException("Login must not be empty");
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Birthday must be in the past");
        }
    }
}