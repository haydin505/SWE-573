package com.infrasave.repository;

import com.infrasave.entity.User;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import static org.springframework.util.ObjectUtils.isEmpty;

/**
 * @author huseyinaydin
 */
@Repository
public class CustomUserRepositoryImpl implements CustomUserRepository {

  private final EntityManager entityManager;

  private final Session session;

  public CustomUserRepositoryImpl(EntityManager entityManager, Session session) {
    this.entityManager = entityManager;
    this.session = session;
  }

  public User getById(Long id) throws Exception {
    Optional<User> user = Optional.ofNullable(session.get(User.class, id));
    return user.orElseThrow(Exception::new);
  }

  public void add(User user) {
    session.save(user);
  }

  @Override
  public Optional<User> getByEmail(String email) {
    Query<User> query = session.createQuery("select u from User u where u.email = :email", User.class);
    query.setParameter("email", email);
      List<User> resultList = query.getResultList();
      if(isEmpty(resultList)){
        return Optional.empty();
      }
    User user = resultList.get(0);
    return Optional.ofNullable(user);
  }
}
