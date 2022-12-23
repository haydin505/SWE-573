package com.infrasave.exception;

import static com.infrasave.enums.ErrorCode.USERNAME_ALREADY_TAKEN_EXCEPTION;

/**
 * @author huseyinaydin
 */
public class UsernameAlreadyTakenException extends AbstractException {

  public UsernameAlreadyTakenException() {
    super(USERNAME_ALREADY_TAKEN_EXCEPTION);
  }
}
