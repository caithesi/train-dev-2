package com.example.demo.error;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
  private final int status;

  public CustomException(int status) {
    this.status = status;
  }

  public CustomException(String message, int status) {
    super(message);
    this.status = status;
  }

  public CustomException(String message, Throwable cause, int status) {
    super(message, cause);
    this.status = status;
  }

  public CustomException(Throwable cause, int status) {
    super(cause);
    this.status = status;
  }
}
