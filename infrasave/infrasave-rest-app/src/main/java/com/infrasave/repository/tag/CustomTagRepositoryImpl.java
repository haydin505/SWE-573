package com.infrasave.repository.tag;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

/**
 * @author huseyinaydin
 */
@Repository
public class CustomTagRepositoryImpl implements CustomTagRepository {

  private final Session session;

  public CustomTagRepositoryImpl(Session session) {
    this.session = session;
  }
}
