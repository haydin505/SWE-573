package com.infrasave.service;

import com.infrasave.bean.RegisterRequest;
import com.infrasave.entity.User;
import com.infrasave.entity.UserRole;
import com.infrasave.exception.UserAlreadyRegisteredException;
import com.infrasave.repository.UserRepository;
import com.infrasave.repository.UserRoleRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author huseyinaydin
 */
@Service
public class AccountService {

  private final UserRepository userRepository;

  private final UserRoleRepository userRoleRepository;

  private final PasswordEncoder passwordEncoder;

  public AccountService(UserRepository userRepository, UserRoleRepository userRoleRepository,
                        PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.userRoleRepository = userRoleRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public void register(RegisterRequest request) throws UserAlreadyRegisteredException {
    Optional<User> userOptional = userRepository.getByEmail(request.email());
    if (userOptional.isPresent()) {
      throw new UserAlreadyRegisteredException();
    }
    User user = new User();
    LocalDateTime now = LocalDateTime.now();
    user.setCreatedAt(now);
    user.setLastUpdatedAt(now);
    user.setPassword(passwordEncoder.encode(request.password()));
    user.setName(request.name());
    user.setSurname(request.surname());
    user.setEmail(request.email());
    user.setBirthDate(request.birthDate());
    UserRole userRole = new UserRole();
    userRole.setCreatedAt(now);
    userRole.setLastUpdatedAt(now);
    userRole.setUser(user);
    userRole.setRole("ROLE_USER");
    user.setRoles(List.of(userRole));
    userRepository.save(user);
  }
}
