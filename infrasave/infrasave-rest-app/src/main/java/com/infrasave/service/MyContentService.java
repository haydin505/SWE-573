package com.infrasave.service;

import com.infrasave.bean.AddMyContentRequest;
import com.infrasave.bean.ContentDTO;
import com.infrasave.bean.TagDTO;
import com.infrasave.entity.Content;
import com.infrasave.entity.MyContent;
import com.infrasave.entity.User;
import com.infrasave.enums.VisibilityLevel;
import com.infrasave.repository.mycontent.MyContentRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

import static com.infrasave.service.Utils.mapCreatorToUserDTO;

/**
 * @author huseyinaydin
 */
@Service
public class MyContentService {

  private final MyContentRepository myContentRepository;

  private final UserService userService;

  private final ContentService contentService;

  private final TagService tagService;

  public MyContentService(MyContentRepository myContentRepository,
                          UserService userService,
                          ContentService contentService, TagService tagService) {
    this.myContentRepository = myContentRepository;
    this.userService = userService;
    this.contentService = contentService;
    this.tagService = tagService;
  }

  public List<ContentDTO> getMyContent() {
    User currentUser = userService.getCurrentUser();
    List<MyContent> myContents = myContentRepository.getMyContent(currentUser);
    return myContents.stream()
                     .filter(myContent -> myContent.getContent().getCreatorId().getId().equals(currentUser.getId())
                                          || !VisibilityLevel.PRIVATE.equals(
                         myContent.getContent().getVisibilityLevel()))
                     .map(myContent -> new ContentDTO(myContent.getContent(),
                                                      Boolean.TRUE,
                                                      mapCreatorToUserDTO(myContent.getContent().getCreatorId()),
                                                      myContent.getContent().getTags().stream().map(
                                                          TagDTO::new).toList()))
                     .toList();
  }

  public void addMyContent(AddMyContentRequest request) {
    User user = userService.getCurrentUser();
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
    User user = userService.getCurrentUser();
    Content content = contentService.getContentById(contentId);
    MyContent myContent = myContentRepository.getMyContentByUserAndContent(user, content);
    myContentRepository.delete(myContent);
  }
}
