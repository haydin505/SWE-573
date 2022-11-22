package com.infrasave.config;

import javax.persistence.EntityManager;
import org.hibernate.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author huseyinaydin
 */
@Configuration
public class DatabaseConfiguration {

  @Bean
  public Session session(EntityManager entityManager) {
    return entityManager.unwrap(Session.class);
  }
}
