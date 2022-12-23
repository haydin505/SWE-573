package com.infrasave.repository.user;

import com.infrasave.entity.User;
import java.util.List;
import java.util.Optional;

/**
 * @author huseyinaydin
 */
public interface CustomUserRepository {

  User getById(Long id) throws Exception;

  void add(User user);

  Optional<User> getByEmail(String email);

  List<User> getUsersByNameAndSurname(List<String> names);

  Optional<User> getByUsername(String username);

  Optional<User> getByResetPasswordToken(String resetPasswordToken);
}
