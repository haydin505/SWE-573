package com.infrasave.service;

import com.infrasave.bean.UserDTO;
import com.infrasave.config.CustomUserDetails;
import com.infrasave.entity.Content;
import com.infrasave.entity.User;
import com.infrasave.enums.FriendRequestStatus;
import com.infrasave.enums.VisibilityLevel;
import com.infrasave.repository.content.ContentRepository;
import com.infrasave.repository.tag.TagRepository;
import com.infrasave.repository.user.UserRepository;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static java.util.Collections.emptyList;
import static org.springframework.util.ObjectUtils.isEmpty;

/**
 * @author huseyinaydin
 */
@Service
public class SearchService {

  private final ContentRepository contentRepository;

  private final UserRepository userRepository;

  private final TagRepository tagRepository;

  private final FriendService friendService;

  public SearchService(ContentRepository contentRepository, UserRepository userRepository,
                       TagRepository tagRepository, FriendService friendService) {
    this.contentRepository = contentRepository;
    this.userRepository = userRepository;
    this.tagRepository = tagRepository;
    this.friendService = friendService;
  }

  private static List<String> getParameters(String query) {
    List<String> params = Arrays.asList(query.split(" "));
    return params;
  }

  private static Long getCurrentUserId() {
    CustomUserDetails principal =
        (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Long currentUserId = principal.getUserId();
    return currentUserId;
  }

  public List<UserDTO> getUsers(String query) {
    if (isEmpty(query)) {
      return emptyList();
    }
    List<String> names = getParameters(query);
    List<User> users = userRepository.getUsersByNameAndSurname(names);
    Long currentUserId = getCurrentUserId();
    User user = userRepository.findById(currentUserId).orElseThrow();
    List<User> currentUserFriendList = friendService.getFriendList(user);
    List<Long> currentUserFriendIdList = currentUserFriendList.stream().map(User::getId).toList();
    return users.stream().map(u -> {
      UserDTO userDTO = new UserDTO(u);
      userDTO.setFriendRequestStatus(
          currentUserFriendIdList.contains(u.getId()) ? FriendRequestStatus.APPROVED : FriendRequestStatus.NONE);
      return userDTO;
    }).toList();
  }

  public List<Content> getContentsByTag(String query) {
    if (isEmpty(query)) {
      return emptyList();
    }
    List<Content> contents = contentRepository.getContentByTagName(query);
    List<Content> result = contents.stream().filter(getContentPredicate()).toList();
    return result;
  }

  public List<Content> getContentsByTitleAndDescription(String query) {
    List<String> params = getParameters(query);
    List<Content> contents = contentRepository.getByTitleOrDescription(params);
    List<Content> result = contents.stream().filter(getContentPredicate()).toList();
    return result;
  }

  private Predicate<Content> getContentPredicate() {
    return content -> {
      if (content.getVisibilityLevel().equals(VisibilityLevel.EVERYONE)) {
        return true;
      }
      if (content.getVisibilityLevel().equals(VisibilityLevel.FRIENDS)) {
        Long currentUserId = getCurrentUserId();
        User user = content.getCreatorId();
        List<Long> friendList = friendService.getFriendList(user).stream().map(User::getId).toList();
        return friendList.contains(currentUserId);
      }
      return false;
    };
  }
}
