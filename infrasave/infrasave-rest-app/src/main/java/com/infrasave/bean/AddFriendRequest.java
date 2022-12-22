package com.infrasave.bean;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * @author huseyinaydin
 */
public record AddFriendRequest(@NotNull @Positive Long requesteeId) {

}
