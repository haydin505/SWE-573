package com.infrasave.bean;

import com.infrasave.entity.User;
import java.time.LocalDateTime;
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

  private String phoneNumber;

  private LocalDateTime birthDate;

  private List<ContentDTO> createdContents;

  private List<String> roles;

  private Integer friendCount;

  private List<UserDTO> friends;

  private FriendDTO friendDTO;

  public UserDTO() {
  }

  public UserDTO(String username, Long userId, String name, String surname, LocalDateTime birthDate, String email,
                 String phoneNumber,
                 List<String> roles, List<ContentDTO> createdContents, List<UserDTO> friends, Integer friendCount,
                 FriendDTO friendDTO) {
    this.username = username;
    this.userId = userId;
    this.name = name;
    this.surname = surname;
    this.birthDate = birthDate;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.roles = roles;
    this.createdContents = createdContents;
    this.friends = friends;
    this.friendCount = friendCount;
    this.friendDTO = friendDTO;
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

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public LocalDateTime getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(LocalDateTime birthDate) {
    this.birthDate = birthDate;
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

  public Integer getFriendCount() {
    return friendCount;
  }

  public void setFriendCount(Integer friendCount) {
    this.friendCount = friendCount;
  }

  public List<UserDTO> getFriends() {
    return friends;
  }

  public void setFriends(List<UserDTO> friends) {
    this.friends = friends;
  }

  public FriendDTO getFriendDTO() {
    return friendDTO;
  }

  public void setFriendDTO(FriendDTO friendDTO) {
    this.friendDTO = friendDTO;
  }
}
