package com.infrasave.exception;

import com.infrasave.enums.ErrorCode;
import org.springframework.http.HttpStatus;

/**
 * @author huseyinaydin
 */
public abstract class AbstractException extends RuntimeException {

  private final String code;

  private final HttpStatus httpStatus;

  public AbstractException(String message, String code, HttpStatus httpStatus) {
    super(message);
    this.httpStatus = httpStatus;
    this.code = code;
  }

  public AbstractException(ErrorCode errorCode) {
    super(errorCode.getErrorDetail());
    this.httpStatus = errorCode.getHttpStatus();
    this.code = errorCode.getErrorCode();
  }

  public String getCode() {
    return code;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }
}
