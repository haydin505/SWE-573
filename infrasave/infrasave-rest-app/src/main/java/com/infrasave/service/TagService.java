package com.infrasave.service;

import com.infrasave.entity.Tag;
import com.infrasave.repository.tag.TagRepository;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * @author huseyinaydin
 */
@Service
public class TagService {

  private final TagRepository tagRepository;

  public TagService(TagRepository tagRepository) {
    this.tagRepository = tagRepository;
  }

  public void addTag(String name, String description, String color) {
    Tag tag = new Tag();
    tag.setName(name);
    tag.setDescription(description);
    tag.setColor(color);
    LocalDateTime now = LocalDateTime.now();
    tag.setCreatedAt(now);
    tag.setLastUpdatedAt(now);
    tagRepository.save(tag);
  }

  public List<Tag> getTags() {
    return tagRepository.findAll();
  }

  public List<Tag> getTagsByIds(Collection<Long> ids) {
    return tagRepository.findAllById(ids);
  }
}
