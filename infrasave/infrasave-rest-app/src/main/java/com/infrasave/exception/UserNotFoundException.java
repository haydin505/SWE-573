package com.infrasave.exception;

import com.infrasave.enums.ErrorCode;

/**
 * @author huseyinaydin
 */
public class UserNotFoundException extends AbstractException {

  public UserNotFoundException() {
    super(ErrorCode.USER_NOT_FOUND_EXCEPTION);
  }
}
