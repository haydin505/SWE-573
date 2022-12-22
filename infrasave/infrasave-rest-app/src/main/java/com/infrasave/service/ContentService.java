package com.infrasave.service;

import com.infrasave.bean.ContentDTO;
import com.infrasave.bean.TagDTO;
import com.infrasave.config.CustomUserDetails;
import com.infrasave.entity.Content;
import com.infrasave.entity.Friend;
import com.infrasave.entity.Tag;
import com.infrasave.entity.User;
import com.infrasave.enums.FriendRequestStatus;
import com.infrasave.enums.VisibilityLevel;
import com.infrasave.exception.NotAuthorizedException;
import com.infrasave.repository.content.ContentRepository;
import com.infrasave.repository.friend.FriendRepository;
import com.infrasave.repository.user.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import static com.infrasave.service.Utils.mapCreatorToUserDTO;
import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;

/**
 * @author huseyinaydin
 */
@Service
public class ContentService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ContentService.class);

  private static final List<VisibilityLevel> validVisibilities =
      List.of(VisibilityLevel.EVERYONE, VisibilityLevel.FRIENDS);

  private final ContentRepository contentRepository;

  private final UserRepository userRepository;

  private final FriendRepository friendRepository;

  private final TagService tagService;

  public ContentService(ContentRepository contentRepository, UserRepository userRepository,
                        FriendRepository friendRepository, TagService tagService) {
    this.contentRepository = contentRepository;
    this.userRepository = userRepository;
    this.friendRepository = friendRepository;
    this.tagService = tagService;
  }

  private static CustomUserDetails getPrincipal() {
    return (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

  public List<Content> getContentByUserId(Long userId) {
    Optional<User> userOptional = userRepository.findById(userId);
    User user = userOptional.orElseThrow();
    List<Content> createdContents = user.getCreatedContents();
    return createdContents;
  }

  public List<ContentDTO> getMyFeed() {
    CustomUserDetails principal = getPrincipal();
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
    List<Long> myContentIds = user.getMyContents().stream().map(myContent -> myContent.getContent().getId()).toList();
    return resultList.stream()
                     .map(content -> {
                       Set<Tag> tags = content.getTags();
                       List<TagDTO> tagDTOs = tags.stream().map(TagDTO::new).toList();
                       return new ContentDTO(content, myContentIds.contains(content.getId()),
                                             mapCreatorToUserDTO(content.getCreatorId()), tagDTOs);
                     })
                     .toList();
  }

  private List<Content> getFriendContents(User user) {
    List<Friend> friends = friendRepository.getFriendListByUser(user);
    List<User> users =
        friends.stream().filter(friend -> friend.getStatus().equals(FriendRequestStatus.APPROVED)).map(friend -> {
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

  public void addContent(Long userId, VisibilityLevel visibilityLevel, String title, String url,
                         String imageUrl, String description, List<Long> tagIds) {
    if (isNull(userId)) {
      return;
    }
    Optional<User> userOptional = userRepository.findById(userId);
    if (userOptional.isEmpty()) {
      return;
    }
    Content content = new Content();
    content.setVisibilityLevel(visibilityLevel);
    content.setTitle(title);
    content.setUrl(url);
    content.setImageUrl(imageUrl);
    content.setDescription(description);
    content.setCreatorId(userOptional.get());
    LocalDateTime now = LocalDateTime.now();
    content.setCreatedAt(now);
    content.setLastUpdatedAt(now);
    addTag(tagIds, content);
    contentRepository.save(content);
  }

  private void addTag(List<Long> tagIds, Content content) {
    if (!ObjectUtils.isEmpty(tagIds)) {
      List<Tag> tags = tagService.getTagsByIds(tagIds);
      if (tags.size() != tagIds.size()) {
        List<Long> foundTagIds = tags.stream().map(Tag::getId).toList();
        Long unknownId = tagIds.stream().filter(id -> !foundTagIds.contains(id)).findAny().orElse(null);
        LOGGER.warn("Unknown id found: {}.", unknownId);
      }
      content.setTags(new HashSet<>(tags));
    } else {
      content.setTags(new HashSet<>());
    }
  }

  public void modifyContent(Long contentId, VisibilityLevel visibilityLevel, String title, String url, String imageUrl,
                            String description, List<Long> tagIds) {
    Content content = contentRepository.findById(contentId).orElseThrow();
    User creatorUser = content.getCreatorId();
    CustomUserDetails principal = getPrincipal();
    if (!principal.getUserId().equals(creatorUser.getId())) {
      throw new NotAuthorizedException();
    }
    LocalDateTime now = LocalDateTime.now();
    content.setLastUpdatedAt(now);
    content.setVisibilityLevel(visibilityLevel);
    content.setTitle(title);
    content.setUrl(url);
    content.setImageUrl(imageUrl);
    content.setDescription(description);
    addTag(tagIds, content);
    contentRepository.save(content);
  }

  public void deleteContent(Long contentId) {
    Optional<Content> contentOptional = contentRepository.findById(contentId);
    Content content = contentOptional.orElseThrow();
    User creatorUser = content.getCreatorId();
    CustomUserDetails principal = getPrincipal();
    if (!principal.getUserId().equals(creatorUser.getId())) {
      throw new NotAuthorizedException();
    }
    contentRepository.delete(content);
  }

  public Content getContentById(Long contentId) {
    Optional<Content> contentOptional = contentRepository.findById(contentId);
    return contentOptional.orElseThrow();
  }
}
