package com.infrasave.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * @author huseyinaydin
 */
@Entity
@Table(name = "tag")
public class Tag extends AbstractEntity {

  @Column(nullable = false, unique = true)
  private String name;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private String color;

  @ManyToMany(mappedBy = "tags")
  private Set<Content> contents = new HashSet<>();

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public Set<Content> getContents() {
    return contents;
  }

  public void setContents(Set<Content> contents) {
    this.contents = contents;
  }
}
