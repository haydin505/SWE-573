package com.infrasave.controller;

import com.infrasave.bean.AddContentRequest;
import com.infrasave.bean.AppResponse;
import com.infrasave.bean.AppResponses;
import com.infrasave.bean.ContentDTO;
import com.infrasave.bean.ModifyContentRequest;
import com.infrasave.config.CustomUserDetails;
import com.infrasave.entity.Content;
import com.infrasave.service.ContentService;
import java.util.List;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huseyinaydin
 */
@RestController
@RequestMapping("contents")
public class ContentController {

  private final ContentService contentService;

  public ContentController(ContentService contentService) {
    this.contentService = contentService;
  }

  @GetMapping("/users/{userId}")
  public AppResponse<List<Content>> getContentByUserId(@PathVariable Long userId) {
    List<Content> contents = contentService.getContentByUserId(userId);
    AppResponse<List<Content>> appResponse = AppResponses.successful(contents);
    return appResponse;
  }

  @GetMapping("/my-feed")
  public AppResponse<List<ContentDTO>> getMyFeed() {
    List<ContentDTO> contents = contentService.getMyFeed();
    AppResponse<List<ContentDTO>> appResponse = AppResponses.successful(contents);
    return appResponse;
  }

  @PostMapping
  public AppResponse addContent(@RequestBody @Validated AddContentRequest request) {
    CustomUserDetails principal =
        (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    contentService.addContent(principal.getUserId(),
                              request.visibilityLevel(),
                              request.title(),
                              request.url(),
                              request.imageUrl(),
                              request.description());
    return AppResponses.successful();
  }

  @PutMapping
  public AppResponse modifyContent(@RequestBody @Validated ModifyContentRequest request) throws Exception {
    contentService.modifyContent(request.contentId(),
                                 request.visibilityLevel(),
                                 request.title(),
                                 request.url(),
                                 request.imageUrl(),
                                 request.description());
    return AppResponses.successful();
  }

  @DeleteMapping("/{contentId}")
  public AppResponse deleteContent(@PathVariable Long contentId) throws Exception {
    contentService.deleteContent(contentId);
    return AppResponses.successful();
  }
}
