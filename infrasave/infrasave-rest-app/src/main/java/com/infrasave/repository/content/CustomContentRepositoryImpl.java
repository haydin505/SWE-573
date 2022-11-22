package com.infrasave.repository.content;

import com.infrasave.entity.Content;
import com.infrasave.entity.User;
import com.infrasave.enums.VisibilityLevel;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

/**
 * @author huseyinaydin
 */
@Repository
public class CustomContentRepositoryImpl implements CustomContentRepository {

  private final Session session;

  public CustomContentRepositoryImpl(Session session) {
    this.session = session;
  }

  @Override
  public List<Content> getByUsersAndVisibilities(List<User> users, List<VisibilityLevel> visibilityLevels) {
    Query<Content> query =
        session.createQuery("FROM Content WHERE creatorId IN (:users) AND visibilityLevel IN (:visibilityLevels)",
                            Content.class);
    query.setParameter("users", users);
    query.setParameter("visibilityLevels", visibilityLevels);
    return query.getResultList();
  }

  @Override
  public List<Content> getByVisibility(List<VisibilityLevel> visibilityLevels) {
    Query<Content> query =
        session.createQuery("FROM Content WHERE visibilityLevel IN (:visibilityLevels)", Content.class);
    query.setParameter("visibilityLevels", visibilityLevels);
    return query.getResultList();
  }
}
