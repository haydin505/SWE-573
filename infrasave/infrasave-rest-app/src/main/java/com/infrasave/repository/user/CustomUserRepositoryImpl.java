package com.infrasave.repository.user;

import com.infrasave.entity.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import static org.springframework.util.ObjectUtils.isEmpty;

/**
 * @author huseyinaydin
 */
@Repository
public class CustomUserRepositoryImpl implements CustomUserRepository {

  private final Session session;

  public CustomUserRepositoryImpl(Session session) {
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
    if (isEmpty(resultList)) {
      return Optional.empty();
    }
    User user = resultList.get(0);
    return Optional.ofNullable(user);
  }

  @Override
  public List<User> getUsersByNameAndSurname(List<String> names) {
    CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
    CriteriaQuery<User> cq = criteriaBuilder.createQuery(User.class);
    Root<User> root = cq.from(User.class);
    List<Predicate> predicates = new ArrayList<>();
    names.forEach(name -> {
      Predicate usernamePredicate = criteriaBuilder.like(root.get("username").as(String.class), "%" + name + "%");
      predicates.add(usernamePredicate);
      Predicate namePredicate = criteriaBuilder.like(root.get("name").as(String.class), "%" + name + "%");
      predicates.add(namePredicate);
      Predicate surnamePredicate = criteriaBuilder.like(root.get("surname").as(String.class), "%" + name + "%");
      predicates.add(surnamePredicate);
    });
    Predicate[] predicatesArr = predicates.toArray(new Predicate[0]);
    Predicate predicate = criteriaBuilder.or(predicatesArr);
    cq.where(predicate);
    Query<User> query = session.createQuery(cq);
    List<User> resultList = query.getResultList();
    return resultList;
  }
}
