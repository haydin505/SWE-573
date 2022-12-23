package com.infrasave.exception;

import static com.infrasave.enums.ErrorCode.RESET_PASSWORD_TOKEN_EXPIRED_EXCEPTION;

/**
 * @author huseyinaydin
 */
public class ResetPasswordTokenExpiredException extends AbstractException {

  public ResetPasswordTokenExpiredException() {
    super(RESET_PASSWORD_TOKEN_EXPIRED_EXCEPTION);
  }
}
