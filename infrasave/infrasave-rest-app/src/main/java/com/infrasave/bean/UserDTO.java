package com.infrasave.bean;

import com.infrasave.entity.User;
import com.infrasave.enums.FriendRequestStatus;
import java.util.List;

/**
 * @author huseyinaydin
 */
public class UserDTO {

  private String username;

  private Long userId;

  private String name;

  private String surname;

  private String email;

  private List<ContentDTO> createdContents;

  private List<String> roles;

  private Integer friendCount;

  private FriendRequestStatus friendRequestStatus;

  public UserDTO() {
  }

  public UserDTO(String username, Long userId, String name, String surname, String email, List<String> roles,
                 List<ContentDTO> createdContents, Integer friendCount, FriendRequestStatus friendRequestStatus) {
    this.username = username;
    this.userId = userId;
    this.name = name;
    this.surname = surname;
    this.email = email;
    this.roles = roles;
    this.createdContents = createdContents;
    this.friendCount = friendCount;
    this.friendRequestStatus = friendRequestStatus;
  }

  public UserDTO(User user) {
    this.username = user.getUsername();
    this.userId = user.getId();
    this.name = user.getName();
    this.surname = user.getSurname();
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Integer getFriendCount() {
    return friendCount;
  }

  public void setFriendCount(Integer friendCount) {
    this.friendCount = friendCount;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public List<ContentDTO> getCreatedContents() {
    return createdContents;
  }

  public void setCreatedContents(List<ContentDTO> createdContents) {
    this.createdContents = createdContents;
  }

  public List<String> getRoles() {
    return roles;
  }

  public void setRoles(List<String> roles) {
    this.roles = roles;
  }

  public FriendRequestStatus getFriendRequestStatus() {
    return friendRequestStatus;
  }

  public void setFriendRequestStatus(FriendRequestStatus friendRequestStatus) {
    this.friendRequestStatus = friendRequestStatus;
  }
}
