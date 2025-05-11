package com.eugenepelipets.popcornlist.controller;

import com.eugenepelipets.popcornlist.dto.UserRequestDto;
import com.eugenepelipets.popcornlist.dto.UserResponseDto;
import com.eugenepelipets.popcornlist.mapper.UserMapper;
import com.eugenepelipets.popcornlist.model.User;
import com.eugenepelipets.popcornlist.service.UserService;
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
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    private User testUser;
    private UserRequestDto requestDto;
    private UserResponseDto responseDto;

    @BeforeEach
    void setUp() {
        // Model
        testUser = User.builder()
                .id(1)
                .email("test@test.com")
                .login("testlogin")
                .name("Test User")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        // Request DTO
        requestDto = new UserRequestDto();
        requestDto.setEmail(testUser.getEmail());
        requestDto.setLogin(testUser.getLogin());
        requestDto.setName(testUser.getName());
        requestDto.setBirthday(testUser.getBirthday());

        // Response DTO
        responseDto = new UserResponseDto();
        responseDto.setId(testUser.getId());
        responseDto.setEmail(testUser.getEmail());
        responseDto.setLogin(testUser.getLogin());
        responseDto.setName(testUser.getName());
        responseDto.setBirthday(testUser.getBirthday());
    }

    @Test
    void createUser_ShouldReturnCreatedUser() {
        when(userMapper.toEntity(requestDto)).thenReturn(testUser);
        when(userService.create(testUser)).thenReturn(testUser);
        when(userMapper.toDto(testUser)).thenReturn(responseDto);

        ResponseEntity<UserResponseDto> response = userController.createUser(requestDto);

        assertEquals(200, response);
        assertEquals(responseDto, response.getBody());
        verify(userMapper).toEntity(requestDto);
        verify(userService).create(testUser);
        verify(userMapper).toDto(testUser);
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() {
        when(userMapper.toEntity(requestDto)).thenReturn(testUser);
        when(userService.update(testUser)).thenReturn(testUser);
        when(userMapper.toDto(testUser)).thenReturn(responseDto);

        ResponseEntity<UserResponseDto> response = userController.updateUser(requestDto);

        assertEquals(responseDto, response.getBody());
        verify(userMapper).toEntity(requestDto);
        verify(userService).update(testUser);
        verify(userMapper).toDto(testUser);
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        when(userService.findAll()).thenReturn(Collections.singletonList(testUser));
        when(userMapper.toDto(testUser)).thenReturn(responseDto);

        ResponseEntity<List<UserResponseDto>> response = userController.getAllUsers();

        assertEquals(1, response.getBody().size());
        assertEquals(responseDto, response.getBody().get(0));
        verify(userService).findAll();
        verify(userMapper).toDto(testUser);
    }

    @Test
    void getUserById_ShouldReturnUser() {
        when(userService.getUserById(1)).thenReturn(testUser);
        when(userMapper.toDto(testUser)).thenReturn(responseDto);

        ResponseEntity<UserResponseDto> response = userController.getUserById(1);

        assertEquals(responseDto, response.getBody());
        verify(userService).getUserById(1);
        verify(userMapper).toDto(testUser);
    }

    @Test
    void addFriend_ShouldCallService() {
        doNothing().when(userService).addFriend(1, 2);

        ResponseEntity<Void> response = userController.addFriend(1, 2);

        assertEquals(200, response);
        verify(userService).addFriend(1, 2);
    }

    @Test
    void removeFriend_ShouldCallService() {
        doNothing().when(userService).removeFriend(1, 2);

        ResponseEntity<Void> response = userController.removeFriend(1, 2);

        assertEquals(200, response);
        verify(userService).removeFriend(1, 2);
    }

    @Test
    void getUserFriends_ShouldReturnFriends() {
        when(userService.getFriendsByUserId(1)).thenReturn(Collections.singletonList(testUser));
        when(userMapper.toDto(testUser)).thenReturn(responseDto);

        ResponseEntity<List<UserResponseDto>> response = userController.getUserFriends(1);

        assertEquals(1, response.getBody().size());
        assertEquals(responseDto, response.getBody().get(0));
        verify(userService).getFriendsByUserId(1);
        verify(userMapper).toDto(testUser);
    }

    @Test
    void getCommonFriends_ShouldReturnCommonFriends() {
        when(userService.findCommonFriends(1, 2)).thenReturn(Collections.singletonList(testUser));
        when(userMapper.toDto(testUser)).thenReturn(responseDto);

        ResponseEntity<List<UserResponseDto>> response = userController.getCommonFriends(1, 2);

        assertEquals(1, response.getBody().size());
        assertEquals(responseDto, response.getBody().get(0));
        verify(userService).findCommonFriends(1, 2);
        verify(userMapper).toDto(testUser);
    }
}
