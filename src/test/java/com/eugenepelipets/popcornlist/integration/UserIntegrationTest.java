package com.eugenepelipets.popcornlist.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.eugenepelipets.popcornlist.model.User;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @SuppressWarnings("null")
    @Test
    void createAndGetUser_ShouldWork() {
        User user = User.builder()
                .email("test@test.com")
                .login("testlogin")
                .name("Test User")
                .birthday(java.time.LocalDate.of(1990, 1, 1))
                .build();
        
        ResponseEntity<User> createResponse = restTemplate.postForEntity("/users", user, User.class);
        assertEquals(HttpStatus.OK, createResponse.getStatusCode());
        
        User createdUser = createResponse.getBody();
        assertNotNull(createdUser.getId());
        
        ResponseEntity<User> getResponse = restTemplate.getForEntity(
                "/users/" + createdUser.getId(), User.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertEquals(createdUser.getId(), getResponse.getBody().getId());
    }
}