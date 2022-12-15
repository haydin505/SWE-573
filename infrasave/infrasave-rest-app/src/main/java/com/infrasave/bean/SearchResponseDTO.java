package com.infrasave.bean;

import java.util.List;

/**
 * @author huseyinaydin
 */
public class SearchResponseDTO {

  private List<UserDTO> users;

  private List<ContentDTO> contentsByTags;

  private List<ContentDTO> contents;

  public List<UserDTO> getUsers() {
    return users;
  }

  public void setUsers(List<UserDTO> users) {
    this.users = users;
  }

  public List<ContentDTO> getContents() {
    return contents;
  }

  public void setContents(List<ContentDTO> contents) {
    this.contents = contents;
  }

  public List<ContentDTO> getContentsByTags() {
    return contentsByTags;
  }

  public void setContentsByTags(List<ContentDTO> contentsByTags) {
    this.contentsByTags = contentsByTags;
  }
}
