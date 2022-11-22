package com.infrasave.service;

import com.infrasave.bean.ContentDTO;
import com.infrasave.config.CustomUserDetails;
import com.infrasave.entity.Content;
import com.infrasave.entity.Friend;
import com.infrasave.entity.User;
import com.infrasave.enums.VisibilityLevel;
import com.infrasave.repository.content.ContentRepository;
import com.infrasave.repository.friend.FriendRepository;
import com.infrasave.repository.user.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;

/**
 * @author huseyinaydin
 */
@Service
public class ContentService {

  private static final List<VisibilityLevel> validVisibilities =
      List.of(VisibilityLevel.EVERYONE, VisibilityLevel.FRIENDS);

  private final ContentRepository contentRepository;

  private final UserRepository userRepository;

  private final FriendRepository friendRepository;

  public ContentService(ContentRepository contentRepository, UserRepository userRepository,
                        FriendRepository friendRepository) {
    this.contentRepository = contentRepository;
    this.userRepository = userRepository;
    this.friendRepository = friendRepository;
  }

  public List<Content> getContentByUserId(Long userId) {
    Optional<User> userOptional = userRepository.findById(userId);
    User user = userOptional.orElseThrow();
    List<Content> createdContents = user.getCreatedContents();
    return createdContents;
  }

  public List<ContentDTO> getMyFeed() {
    CustomUserDetails principal =
        (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (isNull(principal)) {
      return emptyList();
    }
    Long userId = principal.getUserId();
    Optional<User> userOptional = userRepository.findById(userId);
    if (userOptional.isEmpty()) {
      return emptyList();
    }
    User user = userOptional.get();
    List<Content> createdContents = user.getCreatedContents();
    List<Content> friendContents = getFriendContents(user);
    List<Content> everyoneContents = contentRepository.getByVisibility(List.of(VisibilityLevel.EVERYONE));
    everyoneContents =
        everyoneContents.stream().filter(content -> content.getCreatorId().equals(user.getId())).toList();
    List<Content> resultList = new ArrayList<>();
    resultList.addAll(createdContents);
    resultList.addAll(friendContents);
    resultList.addAll(everyoneContents);
    return resultList.stream().map(ContentDTO::new).toList();
  }

  private List<Content> getFriendContents(User user) {
    List<Friend> friends = friendRepository.getFriendListByUser(user);
    List<User> users = friends.stream().map(friend -> {
      if (!friend.getRequestee().getId().equals(user.getId())) {
        return friend.getRequestee();
      }
      if (!friend.getRequester().getId().equals(user.getId())) {
        return friend.getRequester();
      }
      return null;
    }).filter(Objects::nonNull).toList();
    List<Content> friendContents = contentRepository.getByUsersAndVisibilities(users, List.of(VisibilityLevel.FRIENDS));
    return friendContents;
  }
}
