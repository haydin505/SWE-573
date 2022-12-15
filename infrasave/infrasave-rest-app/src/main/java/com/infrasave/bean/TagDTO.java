package com.infrasave.bean;

import com.infrasave.entity.Tag;
import java.time.LocalDateTime;

/**
 * @author huseyinaydin
 */
public record TagDTO(Long id,
                     String name,
                     String description,
                     String color,
                     LocalDateTime createdAt,
                     LocalDateTime lastUpdatedAt) {

  public TagDTO(Tag tag) {
    this(tag.getId(),
         tag.getName(),
         tag.getDescription(),
         tag.getColor(),
         tag.getCreatedAt(),
         tag.getLastUpdatedAt());
  }
}
