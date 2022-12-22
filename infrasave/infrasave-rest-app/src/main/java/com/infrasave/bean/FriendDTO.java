package com.infrasave.bean;

import com.infrasave.entity.Friend;
import com.infrasave.enums.FriendRequestStatus;

/**
 * @author huseyinaydin
 */
public record FriendDTO(Long id, Long requesteeId, Long requesterId, FriendRequestStatus friendRequestStatus,
                        UserDTO requesterDetail) {

  public FriendDTO(Friend friend) {
    this(friend.getId(),
         friend.getRequestee().getId(),
         friend.getRequester().getId(),
         friend.getStatus(),
         new UserDTO(friend.getRequester()));
  }
}
