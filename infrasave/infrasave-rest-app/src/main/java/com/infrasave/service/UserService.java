package com.infrasave.service;

import com.infrasave.bean.ContentDTO;
import com.infrasave.bean.FriendDTO;
import com.infrasave.bean.TagDTO;
import com.infrasave.bean.UserDTO;
import com.infrasave.config.CustomUserDetails;
import com.infrasave.entity.Content;
import com.infrasave.entity.Friend;
import com.infrasave.entity.Tag;
import com.infrasave.entity.User;
import com.infrasave.enums.FriendRequestStatus;
import com.infrasave.enums.VisibilityLevel;
import com.infrasave.repository.user.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static com.infrasave.service.Utils.mapCreatorToUserDTO;
import static java.util.Objects.nonNull;

/**
 * @author huseyinaydin
 */
@Service
public class UserService {

  private final UserRepository userRepository;

  private final FriendService friendService;

  public UserService(UserRepository userRepository, FriendService friendService) {
    this.userRepository = userRepository;
    this.friendService = friendService;
  }

  public User getUserById(Long id) {
    Optional<User> optionalUser = userRepository.findById(id);
    return optionalUser.orElseThrow();
  }

  public User getCurrentUser() {
    CustomUserDetails principal =
        (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Long userId = principal.getUserId();
    User user = getUserById(userId);
    return user;
  }

  public UserDTO getUserDTO(Long id) {
    User creatorUser = getUserById(id);
    List<User> friendList = friendService.getApprovedFriendList(creatorUser);
    List<Long> friendIdList = friendList.stream().map(User::getId).toList();
    List<Content> createdContents = creatorUser.getCreatedContents();
    List<ContentDTO> visibleContents = new ArrayList<>();
    CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext()
                                                                           .getAuthentication().getPrincipal();
    createdContents.forEach(content -> {
      VisibilityLevel visibilityLevel = content.getVisibilityLevel();
      switch (visibilityLevel) {
        case FRIENDS -> {
          if (friendIdList.contains(principal.getUserId())) {
            Set<Tag> tags = content.getTags();
            List<TagDTO> tagDTOs = tags.stream().map(TagDTO::new).toList();
            ContentDTO contentDTO = new ContentDTO(content, null, mapCreatorToUserDTO(creatorUser), tagDTOs);
            visibleContents.add(contentDTO);
          }
        }
        case EVERYONE -> {
          Set<Tag> tags = content.getTags();
          List<TagDTO> tagDTOs = tags.stream().map(TagDTO::new).toList();
          ContentDTO contentDTO = new ContentDTO(content, null, mapCreatorToUserDTO(creatorUser), tagDTOs);
          visibleContents.add(contentDTO);
        }
      }
    });
    User currentUser = getCurrentUser();
    List<Friend> friendEntities = friendService.getFriendEntities(currentUser);
    Optional<Friend> friendOptional = friendEntities
        .stream()
        .filter(f ->
                    (f.getRequestee().getId().equals(currentUser.getId()) && f.getRequester()
                                                                              .getId()
                                                                              .equals(creatorUser.getId())) ||
                    (f.getRequestee().getId().equals(creatorUser.getId()) || f.getRequester()
                                                                              .getId()
                                                                              .equals(currentUser.getId())))
        .findFirst();
    FriendDTO friendDTO = friendOptional.map(FriendDTO::new).orElse(new FriendDTO(null,
                                                                                  creatorUser.getId(),
                                                                                  null,
                                                                                  FriendRequestStatus.NONE,
                                                                                  new UserDTO(currentUser)));
    List<UserDTO> friends = friendList.stream().map(UserDTO::new).toList();
    return new UserDTO(creatorUser.getUsername(),
                       creatorUser.getId(),
                       creatorUser.getName(),
                       creatorUser.getSurname(),
                       creatorUser.getBirthDate(),
                       null,
                       null,
                       visibleContents,
                       friends,
                       friendList.size(),
                       friendDTO);
  }

  public void updateUser(Long userId, String name, String surname, String username, String email,
                         LocalDateTime birthDate) {
    User user = getUserById(userId);
    boolean updated = false;
    if (nonNull(name)) {
      user.setName(name);
      updated = true;
    }
    if (nonNull(surname)) {
      user.setSurname(surname);
      updated = true;
    }
    if (nonNull(username)) {
      user.setUsername(username);
      updated = true;
    }
    if (nonNull(email)) {
      user.setEmail(email);
      updated = true;
    }
    if (nonNull(birthDate)) {
      user.setBirthDate(birthDate);
      updated = true;
    }
    if (updated) {
      userRepository.save(user);
    }
  }
}
