package com.eugenepelipets.popcornlist.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.eugenepelipets.popcornlist.model.User;
import com.eugenepelipets.popcornlist.service.UserService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

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
    void createUser_ShouldReturnCreatedUser() {
        when(userService.create(any(User.class))).thenReturn(testUser);

        ResponseEntity<User> response = userController.createUser(testUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testUser, response.getBody());
        verify(userService, times(1)).create(testUser);
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() {
        when(userService.update(any(User.class))).thenReturn(testUser);

        ResponseEntity<User> response = userController.updateUser(testUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testUser, response.getBody());
        verify(userService, times(1)).update(testUser);
    }

    @SuppressWarnings("null")
    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        List<User> users = Collections.singletonList(testUser);
        when(userService.findAll()).thenReturn(users);

        ResponseEntity<Collection<User>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(testUser, response.getBody().iterator().next());
        verify(userService, times(1)).findAll();
    }

    @Test
    void getUserById_ShouldReturnUser() {
        when(userService.getUserById(1)).thenReturn(testUser);

        ResponseEntity<User> response = userController.getUserById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testUser, response.getBody());
        verify(userService, times(1)).getUserById(1);
    }

    @Test
    void addFriend_ShouldCallService() {
        doNothing().when(userService).addFriend(1, 2);

        ResponseEntity<Void> response = userController.addFriend(1, 2);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).addFriend(1, 2);
    }
}