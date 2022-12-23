package com.infrasave.bean;

import javax.validation.constraints.NotBlank;

/**
 * @author huseyinaydin
 */
public record ResetPasswordRequest(@NotBlank String email) {

}
