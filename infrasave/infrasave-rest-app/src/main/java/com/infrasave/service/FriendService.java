package com.infrasave.service;

import com.infrasave.config.CustomUserDetails;
import com.infrasave.entity.Friend;
import com.infrasave.entity.User;
import com.infrasave.enums.FriendRequestStatus;
import com.infrasave.repository.friend.FriendRepository;
import com.infrasave.repository.user.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * @author huseyinaydin
 */
@Service
public class FriendService {

  private final FriendRepository friendRepository;

  private final UserRepository userRepository;

  public FriendService(FriendRepository friendRepository, UserRepository userRepository) {
    this.friendRepository = friendRepository;
    this.userRepository = userRepository;
  }

  public List<User> getCurrentUserFriendList() {
    CustomUserDetails principal =
        (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Long userId = principal.getUserId();
    Optional<User> userOptional = userRepository.findById(userId);
    User user = userOptional.orElseThrow();
    List<User> friendList = getFriendList(user);
    return friendList;
  }

  public List<User> getFriendList(User user) {
    List<Friend> friends = friendRepository.getFriendListByUser(user);
    List<User> friendList = new ArrayList<>();
    friends.stream().filter(friend -> friend.getStatus().equals(FriendRequestStatus.APPROVED)).forEach(friend -> {
      if (!friend.getRequester().getId().equals(user.getId())) {
        friendList.add(friend.getRequester());
      }
      if (!friend.getRequestee().getId().equals(user.getId())) {
        friendList.add(friend.getRequestee());
      }
    });
    return friendList;
  }
}
