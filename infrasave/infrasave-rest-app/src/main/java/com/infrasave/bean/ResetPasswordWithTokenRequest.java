package com.infrasave.bean;

import javax.validation.constraints.NotBlank;

/**
 * @author huseyinaydin
 */
public record ResetPasswordWithTokenRequest(@NotBlank String token, @NotBlank String password) {

}
