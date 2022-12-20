package com.infrasave.bean;

import com.infrasave.entity.MyContent;
import com.infrasave.enums.VisibilityLevel;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author huseyinaydin
 */
public record MyContentDTO(Long id,
                           Long contentId,
                           LocalDateTime createdAt,
                           LocalDateTime lastUpdatedAt,
                           VisibilityLevel visibilityLevel,
                           String title,
                           String url,
                           String imageUrl,
                           String description,
                           Long creatorId,
                           Boolean myContent,
                           UserDTO creatorUser,
                           List<TagDTO> tags) {

  public MyContentDTO(MyContent myContent, UserDTO userDTO, List<TagDTO> tags) {
    this(myContent.getId(),
         myContent.getContent().getId(),
         myContent.getCreatedAt(),
         myContent.getLastUpdatedAt(),
         myContent.getContent().getVisibilityLevel(),
         myContent.getContent().getTitle(),
         myContent.getContent().getUrl(),
         myContent.getContent().getImageUrl(),
         myContent.getContent().getDescription(),
         myContent.getContent().getCreatorId().getId(),
         true,
         userDTO,
         tags);
  }
}
