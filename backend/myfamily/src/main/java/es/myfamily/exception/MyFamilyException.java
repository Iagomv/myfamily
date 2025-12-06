package es.myfamily.exception;

import org.springframework.http.HttpStatus;

public class MyFamilyException extends RuntimeException {

  private final HttpStatus status;
  private final String message;

  public MyFamilyException(HttpStatus status, String message) {
    super(message);
    this.status = status;
    this.message = message;
  }

  public HttpStatus getStatus() {
    return status;
  }

  public String getMessage() {
    return message;
  }
}