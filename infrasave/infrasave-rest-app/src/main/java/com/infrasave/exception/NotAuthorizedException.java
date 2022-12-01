package com.infrasave.exception;

import org.springframework.http.HttpStatus;

/**
 * @author huseyinaydin
 */
public class NotAuthorizedException extends AbstractException{

  public NotAuthorizedException() {
    super("User not authorized to perform this operation", "80001", HttpStatus.FORBIDDEN);
  }
}
