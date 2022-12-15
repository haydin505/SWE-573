package com.infrasave.bean;

import com.infrasave.entity.Content;
import com.infrasave.enums.VisibilityLevel;
import java.time.LocalDateTime;
import java.util.List;

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
                         Boolean myContent,
                         UserDTO creatorUser,
                         List<TagDTO> tags) {

  public ContentDTO(Content content, Boolean myContent, UserDTO userDTO, List<TagDTO> tagDTO) {
    this(content.getId(),
         content.getCreatedAt(),
         content.getLastUpdatedAt(),
         content.getVisibilityLevel(),
         content.getTitle(),
         content.getUrl(),
         content.getImageUrl(),
         content.getDescription(),
         myContent,
         userDTO,
         tagDTO);
  }
}
