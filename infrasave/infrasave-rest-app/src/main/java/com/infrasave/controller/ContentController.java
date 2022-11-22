package com.infrasave.controller;

import com.infrasave.bean.AppResponse;
import com.infrasave.bean.AppResponses;
import com.infrasave.bean.ContentDTO;
import com.infrasave.entity.Content;
import com.infrasave.service.ContentService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
  public ResponseEntity getContentByUserId(@PathVariable Long userId) {
    List<Content> contents = contentService.getContentByUserId(userId);
    AppResponse<List<Content>> appResponse = AppResponses.successful(contents);
    return ResponseEntity.ok(appResponse);
  }

  @GetMapping("/my-feed")
  public ResponseEntity getMyFeed() {
    List<ContentDTO> contents = contentService.getMyFeed();
    AppResponse<List<ContentDTO>> appResponse = AppResponses.successful(contents);
    return ResponseEntity.ok(appResponse);
  }
}
