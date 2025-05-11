package com.eugenepelipets.popcornlist.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.eugenepelipets.popcornlist.dto.UserRequestDto;
import com.eugenepelipets.popcornlist.dto.UserResponseDto;
import com.eugenepelipets.popcornlist.mapper.UserMapper;
import com.eugenepelipets.popcornlist.model.User;
import com.eugenepelipets.popcornlist.service.UserService;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto dto) {
        User user = userMapper.toEntity(dto);
        User created = userService.create(user);
        return ResponseEntity.ok(userMapper.toDto(created));
    }

    @PutMapping
    public ResponseEntity<UserResponseDto> updateUser(@Valid @RequestBody UserRequestDto dto) {
        User user = userMapper.toEntity(dto);
        User updated = userService.update(user);
        return ResponseEntity.ok(userMapper.toDto(updated));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> dtos = userService.findAll()
            .stream()
            .map(userMapper::toDto)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable int id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Void> addFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.addFriend(id, friendId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Void> removeFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.removeFriend(id, friendId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<List<UserResponseDto>> getUserFriends(@PathVariable int id) {
        List<UserResponseDto> dtos = userService.getFriendsByUserId(id)
            .stream()
            .map(userMapper::toDto)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<List<UserResponseDto>> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        List<UserResponseDto> dtos = userService.findCommonFriends(id, otherId)
            .stream()
            .map(userMapper::toDto)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}