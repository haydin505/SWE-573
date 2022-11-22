package com.infrasave.service;

import com.infrasave.entity.User;
import com.infrasave.repository.user.UserRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * @author huseyinaydin
 */
@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User getUserById(Long id) {
    Optional<User> optionalUser = userRepository.findById(id);
    return optionalUser.orElseThrow();
  }
}
