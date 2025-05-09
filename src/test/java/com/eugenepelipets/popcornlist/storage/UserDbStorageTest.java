package com.eugenepelipets.popcornlist.storage;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.eugenepelipets.popcornlist.exception.NotFoundException;
import com.eugenepelipets.popcornlist.storage.user.UserDbStorage;

@ExtendWith(MockitoExtension.class)
public class UserDbStorageTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private UserDbStorage userDbStorage;

    @Test
    void getUserById_ShouldThrowNotFoundException_WhenUserNotExists() {
        when(jdbcTemplate.queryForRowSet(anyString(), anyInt()))
                .thenReturn(mock(SqlRowSet.class));

        assertThrows(NotFoundException.class, () -> userDbStorage.getUserById(1));
    }

}
