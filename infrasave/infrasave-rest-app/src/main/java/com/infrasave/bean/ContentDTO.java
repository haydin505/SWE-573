package com.infrasave.bean;

import com.infrasave.entity.Content;
import com.infrasave.enums.VisibilityLevel;

/**
 * @author huseyinaydin
 */
public record ContentDTO(VisibilityLevel visibilityLevel,
                         String title,
                         String url,
                         String imageUrl,
                         String description,
                         Long creatorId) {

  public ContentDTO(Content content) {
    this(content.getVisibilityLevel(),
         content.getTitle(),
         content.getUrl(),
         content.getImageUrl(),
         content.getDescription(),
         content.getCreatorId().getId());
  }
}
