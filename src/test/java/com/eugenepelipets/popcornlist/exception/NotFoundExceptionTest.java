package com.eugenepelipets.popcornlist.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class NotFoundExceptionTest {

    @Test
    void constructor_ShouldSetMessage() {
        String message = "Test message";
        NotFoundException exception = new NotFoundException(message);
        
        assertEquals(message, exception.getMessage());
    }
}