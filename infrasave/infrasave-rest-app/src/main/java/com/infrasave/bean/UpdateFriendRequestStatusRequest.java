package com.infrasave.bean;

import com.infrasave.enums.FriendRequestStatus;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * @author huseyinaydin
 */
public record UpdateFriendRequestStatusRequest(@NotNull @Positive Long id,
                                               @NotNull FriendRequestStatus friendRequestStatus) {

}
