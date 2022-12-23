package com.infrasave.enums;

import org.springframework.http.HttpStatus;

/**
 * @author huseyinaydin
 */
public enum ErrorCode {
  USER_ALREADY_REGISTERED_EXCEPTION("User already registered.", "80000", HttpStatus.FORBIDDEN),
  USERNAME_ALREADY_TAKEN_EXCEPTION("Username already taken.", "80001", HttpStatus.FORBIDDEN),
  USER_NOT_FOUND_EXCEPTION("User not found.", "80002", HttpStatus.NOT_FOUND),
  RESET_PASSWORD_TOKEN_EXPIRED_EXCEPTION("Reset password token expired.", "80003", HttpStatus.FORBIDDEN);

  private final String errorDetail;

  private final String errorCode;

  private final HttpStatus httpStatus;

  ErrorCode(String errorDetail, String errorCode, HttpStatus httpStatus) {
    this.errorDetail = errorDetail;
    this.errorCode = errorCode;
    this.httpStatus = httpStatus;
  }

  public String getErrorDetail() {
    return errorDetail;
  }

  public String getErrorCode() {
    return errorCode;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }
}
