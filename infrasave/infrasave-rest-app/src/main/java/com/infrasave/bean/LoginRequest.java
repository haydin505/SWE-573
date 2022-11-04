package com.infrasave.bean;

import javax.validation.constraints.NotBlank;

/**
 * @author huseyinaydin
 */
public record LoginRequest(@NotBlank String email, @NotBlank String password) {

}
