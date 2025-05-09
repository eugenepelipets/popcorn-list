package com.eugenepelipets.popcornlist.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.eugenepelipets.popcornlist.exception.*;
import com.eugenepelipets.popcornlist.model.ErrorResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class ErrorHandlerTest {

    @InjectMocks
    private ErrorHandler errorHandler;

    @Test
    void handleValidationExceptions_ShouldReturnBadRequest() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(mock(org.springframework.validation.BindingResult.class));

        ErrorResponse response = errorHandler.handleValidationExceptions(ex);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST.value(), 400);
    }

    @Test
    void handleValidationExceptions_ShouldReturnCorrectMessage() {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("object", "field", "default message");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        MethodParameter methodParameter = mock(MethodParameter.class);
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(methodParameter, bindingResult);

        ErrorResponse response = errorHandler.handleValidationExceptions(ex);

        assertEquals("Validation error: field: default message", response.getError());
    }

    @Test
    void handleNotFoundException_ShouldReturnNotFound() {
        NotFoundException ex = new NotFoundException("Not found");

        ErrorResponse response = errorHandler.handleNotFoundException(ex);

        assertEquals("Not found", response.getError());
    }

    @Test
    void handleValidationException_ShouldReturnBadRequest() {
        ValidationException ex = new ValidationException("Invalid");

        ErrorResponse response = errorHandler.handleValidationException(ex);

        assertEquals("Invalid", response.getError());
    }

    @Test
    void handleAlreadyExistException_ShouldReturnConflict() {
        AlreadyExistException ex = new AlreadyExistException("Exists");

        ErrorResponse response = errorHandler.handleAlreadyExistException(ex);

        assertEquals("Exists", response.getError());
    }

    @Test
    void handleThrowable_ShouldReturnInternalError() {
        Throwable ex = new RuntimeException("Error");

        ErrorResponse response = errorHandler.handleThrowable(ex);

        assertEquals("An unexpected error occurred.", response.getError());
    }
}