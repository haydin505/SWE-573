package com.infrasave.repository.user;

import com.infrasave.entity.User;
import java.util.Optional;

/**
 * @author huseyinaydin
 */
public interface CustomUserRepository {

  User getById(Long id) throws Exception;

  void add(User user);

  Optional<User> getByEmail(String email);
}
