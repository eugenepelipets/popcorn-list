package com.eugenepelipets.popcornlist.exception;

public class ValidationException extends RuntimeException {
    private final String parameter;

    public ValidationException(String parameter, String message) {
        super(message);
        this.parameter = parameter;
    }

    public ValidationException(String string) {
        super(string);
        this.parameter = string;
    }

    public String getParameter() {
        return parameter;
    }
}