package com.infrasave.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author huseyinaydin
 */
@Entity
@Table(name = "user_role")
public class UserRole extends AbstractEntity {

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  private String Role;

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getRole() {
    return Role;
  }

  public void setRole(String role) {
    Role = role;
  }
}
