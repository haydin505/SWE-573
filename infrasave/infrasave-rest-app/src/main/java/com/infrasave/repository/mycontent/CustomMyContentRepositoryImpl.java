package com.infrasave.repository.mycontent;

import com.infrasave.entity.Content;
import com.infrasave.entity.MyContent;
import com.infrasave.entity.User;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

/**
 * @author huseyinaydin
 */
@Repository
public class CustomMyContentRepositoryImpl implements CustomMyContentRepository {

  private final Session session;

  public CustomMyContentRepositoryImpl(Session session) {
    this.session = session;
  }

  @Override
  public List<MyContent> getMyContent(User user) {
    Query<MyContent> query = session.createQuery("FROM MyContent WHERE user = :user", MyContent.class);
    query.setParameter("user", user);
    return query.getResultList();
  }

  @Override
  public MyContent getMyContentByUserAndContent(User user, Content content) {
    Query<MyContent> query =
        session.createQuery("FROM MyContent m WHERE m.user = :user AND content = :content", MyContent.class);
    query.setParameter("user", user);
    query.setParameter("content", content);
    return query.getSingleResult();
  }
}
