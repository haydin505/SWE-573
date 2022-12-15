package com.infrasave.service;

import com.infrasave.bean.AddMyContentRequest;
import com.infrasave.bean.MyContentDTO;
import com.infrasave.config.CustomUserDetails;
import com.infrasave.entity.Content;
import com.infrasave.entity.MyContent;
import com.infrasave.entity.User;
import com.infrasave.repository.mycontent.MyContentRepository;
import com.infrasave.repository.user.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static com.infrasave.service.Utils.mapCreatorToUserDTO;

/**
 * @author huseyinaydin
 */
@Service
public class MyContentService {

  private final MyContentRepository myContentRepository;

  private final UserRepository userRepository;

  private final UserService userService;

  private final ContentService contentService;

  public MyContentService(MyContentRepository myContentRepository, UserRepository userRepository,
                          UserService userService, ContentService contentService) {
    this.myContentRepository = myContentRepository;
    this.userRepository = userRepository;
    this.userService = userService;
    this.contentService = contentService;
  }

  public List<MyContentDTO> getMyContent() {
    User user = getCurrentUser();
    List<MyContent> myContents = myContentRepository.getMyContent(user);
    return myContents.stream()
                     .map(myContent -> new MyContentDTO(myContent,
                                                        mapCreatorToUserDTO(myContent.getContent().getCreatorId())))
                     .toList();
  }

  private User getCurrentUser() {
    CustomUserDetails principal =
        (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Long userId = principal.getUserId();
    Optional<User> userOptional = userRepository.findById(userId);
    return userOptional.orElseThrow();
  }

  public void addMyContent(AddMyContentRequest request) {
    User user = getCurrentUser();
    MyContent myContent = new MyContent();
    Content content = contentService.getContentById(request.contentId());
    myContent.setContent(content);
    myContent.setUser(user);
    LocalDateTime now = LocalDateTime.now();
    myContent.setCreatedAt(now);
    myContent.setLastUpdatedAt(now);
    myContentRepository.save(myContent);
  }

  public void deleteMyContent(Long contentId) {
    User user = getCurrentUser();
    Content content = contentService.getContentById(contentId);
    MyContent myContent = myContentRepository.getMyContentByUserAndContent(user, content);
    myContentRepository.delete(myContent);
  }
}
