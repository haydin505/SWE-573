package com.infrasave.service;

import com.infrasave.config.CustomUserDetails;
import com.infrasave.entity.Friend;
import com.infrasave.entity.User;
import com.infrasave.enums.FriendRequestStatus;
import com.infrasave.exception.NotAuthorizedException;
import com.infrasave.repository.friend.FriendRepository;
import com.infrasave.repository.user.UserRepository;
import java.time.LocalDateTime;
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
    List<User> friendList = getApprovedFriendList(user);
    return friendList;
  }

  public List<User> getApprovedFriendList(User user) {
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

  public List<Friend> getRequesteePendingFriendRequests() {
    User user = getCurrentUser();
    List<Friend> friendList = friendRepository.getFriendListByUser(user);
    return friendList.stream().filter(friend -> friend.getStatus().equals(FriendRequestStatus.PENDING)).toList();
  }

  private User getCurrentUser() {
    CustomUserDetails principal =
        (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return userRepository.findById(principal.getUserId()).orElseThrow();
  }

  private Long getCurrentUserId() {
    CustomUserDetails principal =
        (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return principal.getUserId();
  }

  public void updateFriendRequestStatus(Long id, FriendRequestStatus friendRequestStatus) {
    User currentUser = getCurrentUser();
    Friend friend = friendRepository.findById(id).orElseThrow();
    if (!friend.getRequestee().getId().equals(currentUser.getId())) {
      throw new NotAuthorizedException();
    }
    friend.setStatus(friendRequestStatus);
    friendRepository.save(friend);
  }

  public void deleteFriend(Long id) {
    Long currentUserId = getCurrentUserId();
    Friend friend = friendRepository.findById(id).orElseThrow();
    if (currentUserId.equals(friend.getRequestee().getId()) ||
        currentUserId.equals(friend.getRequester().getId())) {
      friendRepository.delete(friend);
    }
    throw new NotAuthorizedException();
  }

  public void addFriend(Long requesteeId) {
    User currentUser = getCurrentUser();
    User requesteeUser = userRepository.findById(requesteeId).orElseThrow();
    Friend friend = new Friend();
    friend.setStatus(FriendRequestStatus.PENDING);
    LocalDateTime now = LocalDateTime.now();
    friend.setCreatedAt(now);
    friend.setLastUpdatedAt(now);
    friend.setRequester(currentUser);
    friend.setRequestee(requesteeUser);
    friendRepository.save(friend);
  }

  public List<Friend> getFriendEntities(User user) {
    return friendRepository.getFriendListByUser(user);
  }
}
