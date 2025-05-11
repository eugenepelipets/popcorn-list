package com.eugenepelipets.popcornlist.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.eugenepelipets.popcornlist.exception.*;
import com.eugenepelipets.popcornlist.model.User;
import com.eugenepelipets.popcornlist.storage.user.UserDbStorage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserDbStorage userDbStorage;
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public User create(User user) {
        validateUser(user);
        return userDbStorage.create(user);
    }

    @Transactional
    public User update(User user) {
        validateUser(user);
        return userDbStorage.update(user);
    }

    private void validateUser(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidationException("Invalid email format");
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            throw new ValidationException("Login cannot be empty");
        }
    }

    public Collection<User> findAll() {
        return jdbcTemplate.query("SELECT * FROM users", this::mapRowToUser);
    }

    @Transactional
    public void addFriend(int userId, int friendId) {
        validateFriendship(userId, friendId);

        try {
            int updated = jdbcTemplate.update(
                    "INSERT INTO friends (user_id, friend_id) VALUES (?, ?)",
                    userId, friendId);
            if (updated == 0) {
                throw new DataOperationException("Failed to add friend");
            }
        } catch (DataAccessException e) {
            throw new DataOperationException("Friend operation failed", e);
        }
    }

    @Transactional
    public void removeFriend(int userId, int friendId) {
        validateFriendship(userId, friendId);

        try {
            int updated = jdbcTemplate.update(
                    "DELETE FROM friends WHERE user_id = ? AND friend_id = ?",
                    userId, friendId);
            if (updated == 0) {
                throw new NotFoundException("Friendship not found");
            }
        } catch (DataAccessException e) {
            throw new DataOperationException("Friend removal failed", e);
        }
    }

    private void validateFriendship(int userId, int friendId) {
        if (userId <= 0 || friendId <= 0) {
            throw new ValidationException("user IDs", "IDs must be positive");
        }

        if (userId == friendId) {
            throw new ValidationException("user IDs", "Cannot add yourself as friend");
        }

        try {
            getUserById(userId);
            getUserById(friendId);
        } catch (NotFoundException e) {
            throw new ValidationException("user IDs", "One or both users don't exist");
        }

        if (friendshipExists(userId, friendId)) {
            throw new AlreadyExistException("Friendship already exists");
        }
    }

    boolean friendshipExists(int userId, int friendId) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM friends WHERE user_id = ? AND friend_id = ?",
                Integer.class, userId, friendId);
        return count != null && count > 0;
    }

    public Collection<User> getFriendsByUserId(int userId) {
        return jdbcTemplate.query(
                "SELECT u.* FROM users u JOIN friends f ON u.id = f.friend_id WHERE f.user_id = ?",
                this::mapRowToUser, userId);
    }

    public List<User> findCommonFriends(int userId1, int userId2) {
        return jdbcTemplate.query(
                """
                        SELECT u.* FROM users u
                        JOIN friends f1 ON u.id = f1.friend_id AND f1.user_id = ?
                        JOIN friends f2 ON u.id = f2.friend_id AND f2.user_id = ?
                        """,
                this::mapRowToUser, userId1, userId2);
    }

    public User getUserById(int id) {
        if (id <= 0)
            throw new ValidationException("Invalid user ID");
        return userDbStorage.getUserById(id);
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
}