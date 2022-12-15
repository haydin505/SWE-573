package com.infrasave.controller;

import com.infrasave.bean.AddTagRequest;
import com.infrasave.bean.AppResponse;
import com.infrasave.bean.AppResponses;
import com.infrasave.entity.Tag;
import com.infrasave.service.TagService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huseyinaydin
 */
@RestController
@RequestMapping("tags")
public class TagController {

  private final TagService tagService;

  public TagController(TagService tagService) {
    this.tagService = tagService;
  }

  @GetMapping
  public AppResponse<List<Tag>> getTags() {
    List<Tag> tags = tagService.getTags();
    return AppResponses.successful(tags);
  }

  @PostMapping
  public AppResponse addTag(AddTagRequest request) {
    tagService.addTag(request.name(), request.description(), request.color());
    return AppResponse.successful();
  }
}
