package com.infrasave.bean;

import com.infrasave.entity.Content;
import com.infrasave.enums.VisibilityLevel;
import java.time.LocalDateTime;

/**
 * @author huseyinaydin
 */
public record ContentDTO(Long id,
                         LocalDateTime createdAt,
                         LocalDateTime lastUpdatedAt,
                         VisibilityLevel visibilityLevel,
                         String title,
                         String url,
                         String imageUrl,
                         String description,
                         Long creatorId) {

  public ContentDTO(Content content) {
    this(content.getId(),
         content.getCreatedAt(),
         content.getLastUpdatedAt(),
         content.getVisibilityLevel(),
         content.getTitle(),
         content.getUrl(),
         content.getImageUrl(),
         content.getDescription(),
         content.getCreatorId().getId());
  }
}
