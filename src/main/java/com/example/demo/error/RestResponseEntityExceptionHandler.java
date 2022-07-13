package com.example.demo.error;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler({AccessDeniedException.class})
  public ResponseEntity<Object> handleAccessDeniedException(
    Exception ex, WebRequest request) {
    return new ResponseEntity<>(
      "Access denied message here", new HttpHeaders(), HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler({CustomException.class})
  public ResponseEntity<Object> handleCustomException(
    CustomException ex, WebRequest request) {
    return new ResponseEntity<>(
      ex.getMessage(), new HttpHeaders(), ex.getStatus());
  }

  @ExceptionHandler({RuntimeException.class})
  public ResponseEntity<Object> handleRuntimeException(
    CustomException ex, WebRequest request) {
    return new ResponseEntity<>(
      "some_thing_crash", new HttpHeaders(), ex.getStatus());
  }
}
