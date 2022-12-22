package com.infrasave.bean;

import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

/**
 * @author huseyinaydin
 */
public record RegisterRequest(@NotBlank String name,
                              @NotBlank String surname,
                              @NotBlank String username,
                              @NotBlank String email,
                              @NotBlank String password,
                              @NotBlank String phoneNumber,
                              @NotNull @PastOrPresent LocalDateTime birthDate) {

}
