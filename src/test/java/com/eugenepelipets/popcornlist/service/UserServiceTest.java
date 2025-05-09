package com.eugenepelipets.popcornlist.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.eugenepelipets.popcornlist.exception.AlreadyExistException;
import com.eugenepelipets.popcornlist.exception.ValidationException;
import com.eugenepelipets.popcornlist.model.User;
import com.eugenepelipets.popcornlist.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDbStorage userDbStorage;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1)
                .email("test@test.com")
                .login("testlogin")
                .name("Test User")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
    }

    @Test
    void createUser_ShouldReturnUser() {
        when(userDbStorage.create(any(User.class))).thenReturn(testUser);

        User result = userService.create(testUser);

        assertEquals(testUser, result);
        verify(userDbStorage, times(1)).create(testUser);
    }

    @Test
    void addFriend_ShouldCallJdbcTemplate() {
        when(jdbcTemplate.update(anyString(), eq(1), eq(2))).thenReturn(1);

        userService.addFriend(1, 2);

        verify(jdbcTemplate, times(1)).update(anyString(), eq(1), eq(2));
    }

    @SuppressWarnings("unchecked")
    @Test
    void getFriendsByUserId_ShouldReturnFriends() {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(1)))
                .thenReturn(Collections.singletonList(testUser));

        Collection<User> result = userService.getFriendsByUserId(1);

        assertEquals(1, result.size());
        assertEquals(testUser, result.iterator().next());
    }

    @Test
    void updateUser_ShouldReturnUser() {
        when(userDbStorage.update(any(User.class))).thenReturn(testUser);

        User result = userService.update(testUser);

        assertEquals(testUser, result);
        verify(userDbStorage, times(1)).update(testUser);
    }

    @Test
    void removeFriend_ShouldCallJdbcTemplate() {
        when(jdbcTemplate.update(anyString(), eq(1), eq(2))).thenReturn(1);

        userService.removeFriend(1, 2);

        verify(jdbcTemplate, times(1)).update(anyString(), eq(1), eq(2));
    }

    @SuppressWarnings("unchecked")
    @Test
    void findCommonFriends_ShouldReturnFriends() {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(1), eq(2)))
                .thenReturn(Collections.singletonList(testUser));

        List<User> result = userService.findCommonFriends(1, 2);

        assertEquals(1, result.size());
        assertEquals(testUser, result.get(0));
    }

    @Test
    void validateFriendship_ShouldThrowForInvalidIds() {
        assertThrows(ValidationException.class, () -> userService.addFriend(0, 1));
        assertThrows(ValidationException.class, () -> userService.addFriend(1, 1));
    }

    @Test
    void createUser_ShouldThrowValidationException_ForInvalidEmail() {
        User user = User.builder()
                .email("invalid")
                .login("valid")
                .birthday(LocalDate.now().minusYears(20))
                .build();

        assertThrows(ValidationException.class, () -> userService.create(user));
    }

    @Test
    void addFriend_ShouldThrowAlreadyExistException_WhenFriendshipExists() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), anyInt(), anyInt()))
                .thenReturn(1);

        assertThrows(AlreadyExistException.class, () -> userService.addFriend(1, 2));
    }
}