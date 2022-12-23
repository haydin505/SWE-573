package com.infrasave.bean;

import java.time.LocalDateTime;
import javax.validation.constraints.PastOrPresent;

/**
 * @author huseyinaydin
 */
public record UpdateUserRequest(String username,
                                String name,
                                String surname,
                                String phoneNumber,
                                @PastOrPresent LocalDateTime birthDate) {

}
