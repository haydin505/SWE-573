package com.infrasave.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.infrasave.enums.VisibilityLevel;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author huseyinaydin
 */
@Entity
@Table(name = "content",
       indexes = {
           @Index(name = "content_visibility_level", columnList = "visibility_level")
       })
public class Content extends AbstractEntity {

  @Column(name = "visibility_level", nullable = false)
  @Enumerated(EnumType.STRING)
  private VisibilityLevel visibilityLevel;

  @Column(nullable = false)
  private String title;

  @Lob
  @Column(nullable = false)
  private String url;

  @Lob
  private String imageUrl;

  @Lob
  private String description;

  @JsonIgnore
  @ManyToOne(targetEntity = User.class)
  @JoinColumn(name = "creator_id", nullable = false)
  private User creatorId;

  @JsonIgnore
  @ManyToMany
  @JoinTable(
      name = "content_tag",
      joinColumns = {@JoinColumn(name = "content_id")},
      inverseJoinColumns = {@JoinColumn(name = "tag_id")})
  private Set<Tag> tags = new HashSet<>();

  public VisibilityLevel getVisibilityLevel() {
    return visibilityLevel;
  }

  public void setVisibilityLevel(VisibilityLevel visibilityLevel) {
    this.visibilityLevel = visibilityLevel;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public User getCreatorId() {
    return creatorId;
  }

  public void setCreatorId(User creatorId) {
    this.creatorId = creatorId;
  }

  public Set<Tag> getTags() {
    return tags;
  }

  public void setTags(Set<Tag> tags) {
    this.tags = tags;
  }
}
