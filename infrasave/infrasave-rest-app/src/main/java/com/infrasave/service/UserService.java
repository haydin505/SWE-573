package com.infrasave.service;

import com.infrasave.bean.ContentDTO;
import com.infrasave.bean.TagDTO;
import com.infrasave.bean.UserDTO;
import com.infrasave.config.CustomUserDetails;
import com.infrasave.entity.Content;
import com.infrasave.entity.Tag;
import com.infrasave.entity.User;
import com.infrasave.enums.FriendRequestStatus;
import com.infrasave.enums.VisibilityLevel;
import com.infrasave.repository.user.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static com.infrasave.service.Utils.mapCreatorToUserDTO;

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

  public UserDTO getUserDTOByContent(Long id) {
    User creatorUser = getUserById(id);
    List<User> friendList = friendService.getFriendList(creatorUser);
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
    return new UserDTO(creatorUser.getUsername(), creatorUser.getId(), creatorUser.getName(), creatorUser.getSurname(),
                       null, null, visibleContents, friendList.size(),
                       friendIdList.contains(principal.getUserId()) ? FriendRequestStatus.APPROVED
                           : FriendRequestStatus.NONE);
  }
}
