package com.infrasave.entity;

import com.infrasave.enums.FriendRequestStatus;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * @author huseyinaydin
 */
@Entity
@Table(name = "friend",
       uniqueConstraints = {
           @UniqueConstraint(name = "friend_requester_requestee_uk", columnNames = {"requester", "requestee"})
       },
       indexes = {
           @Index(name = "friend_status_ix", columnList = "status")
       })
public class Friend extends AbstractEntity {

  @ManyToOne
  @JoinColumn(name = "requester", nullable = false)
  private User requester;

  @ManyToOne
  @JoinColumn(name = "requestee", nullable = false)
  private User requestee;

  @Column(nullable = false)
  private FriendRequestStatus status;

  public User getRequester() {
    return requester;
  }

  public void setRequester(User requester) {
    this.requester = requester;
  }

  public User getRequestee() {
    return requestee;
  }

  public void setRequestee(User requestee) {
    this.requestee = requestee;
  }

  public FriendRequestStatus getStatus() {
    return status;
  }

  public void setStatus(FriendRequestStatus status) {
    this.status = status;
  }
}
