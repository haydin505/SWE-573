package com.infrasave.repository.friend;

import com.infrasave.entity.Friend;
import com.infrasave.entity.User;
import com.infrasave.enums.FriendRequestStatus;
import java.util.List;
import java.util.Objects;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

/**
 * @author huseyinaydin
 */
@Repository
public class CustomFriendRepositoryImpl implements CustomFriendRepository{

  private final Session session;

  public CustomFriendRepositoryImpl(Session session) {
    this.session = session;
  }

  @Override
  public List<Friend> getFriendListByUser(User user) {
    Query<Friend> query = session.createQuery(
        "FROM Friend WHERE requestee = :requestee OR requester = :requester", Friend.class);
    query.setParameter("requestee", user);
    query.setParameter("requester", user);
    return query.getResultList();
  }
}
