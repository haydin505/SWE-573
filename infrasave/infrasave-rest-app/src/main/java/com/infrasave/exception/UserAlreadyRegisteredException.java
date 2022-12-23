package com.infrasave.exception;

import static com.infrasave.enums.ErrorCode.USER_ALREADY_REGISTERED_EXCEPTION;

/**
 * @author huseyinaydin
 */
public class UserAlreadyRegisteredException extends AbstractException {

  public UserAlreadyRegisteredException() {
    super(USER_ALREADY_REGISTERED_EXCEPTION);
  }
}
