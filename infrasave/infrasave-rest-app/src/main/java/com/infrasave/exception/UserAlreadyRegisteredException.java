package com.infrasave.exception;

import org.springframework.http.HttpStatus;

/**
 * @author huseyinaydin
 */
public class UserAlreadyRegisteredException extends AbstractException {

  public UserAlreadyRegisteredException() {
    super("User already registered", "80000", HttpStatus.FORBIDDEN);
  }
}
