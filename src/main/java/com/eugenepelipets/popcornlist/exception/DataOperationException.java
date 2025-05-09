package com.eugenepelipets.popcornlist.exception;

public class DataOperationException extends RuntimeException {
  public DataOperationException(String message, Throwable cause) {
    super(message, cause);
  }

  public DataOperationException(String message) {
        super(message);
    }
    
}