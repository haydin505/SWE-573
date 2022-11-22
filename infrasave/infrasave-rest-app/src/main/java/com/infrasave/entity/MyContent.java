package com.infrasave.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * @author huseyinaydin
 */
@Entity
@Table(name = "my_content",
       uniqueConstraints = {
           @UniqueConstraint(name = "my_content_content_id_user_id_uk", columnNames = {"content_id", "user_id"})
       })
public class MyContent extends AbstractEntity {

  @ManyToOne
  @JoinColumn(name = "content_id", nullable = false)
  private Content content;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  public Content getContent() {
    return content;
  }

  public void setContent(Content content) {
    this.content = content;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
