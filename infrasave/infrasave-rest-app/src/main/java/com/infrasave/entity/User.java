package com.infrasave.entity;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * @author huseyinaydin
 */
@Entity
@Table(name = "user",
       uniqueConstraints = {
           @UniqueConstraint(name = "user_email_uk", columnNames = {"email"})
       },
       indexes = {
           @Index(name = "user_name_ix", columnList = "name"),
           @Index(name = "user_surname_ix", columnList = "surname")
       })
public class User extends AbstractEntity {

  @Column(nullable = false)
  private String username;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String surname;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private LocalDateTime birthDate;

  @Column(name = "roles", nullable = false)
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<UserRole> roles;

  @OneToMany(mappedBy = "creatorId")
  private List<Content> createdContents;

  @OneToMany(mappedBy = "user")
  private List<MyContent> myContents;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
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

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public LocalDateTime getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(LocalDateTime birthDate) {
    this.birthDate = birthDate;
  }

  public List<UserRole> getRoles() {
    return roles;
  }

  public void setRoles(List<UserRole> roles) {
    this.roles = roles;
  }

  public List<Content> getCreatedContents() {
    return createdContents;
  }

  public void setCreatedContents(List<Content> createdContents) {
    this.createdContents = createdContents;
  }

  public List<MyContent> getMyContents() {
    return myContents;
  }

  public void setMyContents(List<MyContent> myContents) {
    this.myContents = myContents;
  }
}
