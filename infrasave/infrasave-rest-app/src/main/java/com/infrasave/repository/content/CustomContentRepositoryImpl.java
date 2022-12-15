package com.infrasave.repository.content;

import com.infrasave.entity.Content;
import com.infrasave.entity.User;
import com.infrasave.enums.VisibilityLevel;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;
import static org.springframework.util.ObjectUtils.isEmpty;

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

  @Override
  public List<Content> getContentByTagName(String name) {
    if (isNull(name)) {
      return emptyList();
    }
    Query<Content> query =
        session.createQuery("SELECT c FROM Content c JOIN c.tags t WHERE t.name = :name", Content.class);
    query.setParameter("name", name);
    return query.getResultList();
  }

  @Override
  public List<Content> getByTitleOrDescription(List<String> params) {
    if (isEmpty(params) || params.stream().anyMatch(Objects::isNull)) {
      return emptyList();
    }
    CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
    CriteriaQuery<Content> cq = criteriaBuilder.createQuery(Content.class);
    Root<Content> root = cq.from(Content.class);
    List<Predicate> predicates = new ArrayList<>();
    params.forEach(name -> {
      Predicate usernamePredicate = criteriaBuilder.like(root.get("title").as(String.class), "%" + name + "%");
      predicates.add(usernamePredicate);
      Predicate namePredicate = criteriaBuilder.like(root.get("description").as(String.class), "%" + name + "%");
      predicates.add(namePredicate);
    });
    Predicate[] predicatesArr = predicates.toArray(new Predicate[0]);
    Predicate predicate = criteriaBuilder.or(predicatesArr);
    cq.where(predicate);
    Query<Content> query = session.createQuery(cq);
    List<Content> resultList = query.getResultList();
    return resultList;
  }
}
