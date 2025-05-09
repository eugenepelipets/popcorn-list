package com.eugenepelipets.popcornlist.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eugenepelipets.popcornlist.model.User;
import com.eugenepelipets.popcornlist.service.UserService;

import jakarta.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        log.info("POST /users - Creating new user: {}", user);
        User createdUser = userService.create(user);
        return ResponseEntity.ok(createdUser);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        log.info("PUT /users - Updating user with ID: {}", user.getId());
        User updatedUser = userService.update(user);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping
    public ResponseEntity<Collection<User>> getAllUsers() {
        log.info("GET /users - Retrieving all users");
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        log.info("GET /users/{} - Retrieving user by ID", id);
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Void> addFriend(
            @PathVariable int id,
            @PathVariable int friendId) {
        log.info("PUT /users/{}/friends/{} - Adding friend", id, friendId);
        userService.addFriend(id, friendId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Void> removeFriend(
            @PathVariable int id,
            @PathVariable int friendId) {
        log.info("DELETE /users/{}/friends/{} - Removing friend", id, friendId);
        userService.removeFriend(id, friendId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<Collection<User>> getUserFriends(@PathVariable int id) {
        log.info("GET /users/{}/friends - Retrieving user's friends", id);
        return ResponseEntity.ok(userService.getFriendsByUserId(id));
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<Collection<User>> getCommonFriends(
            @PathVariable int id,
            @PathVariable int otherId) {
        log.info("GET /users/{}/friends/common/{} - Retrieving common friends", id, otherId);
        return ResponseEntity.ok(userService.findCommonFriends(id, otherId));
    }
}