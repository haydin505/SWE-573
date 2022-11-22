package com.infrasave.config;

import com.infrasave.entity.User;
import com.infrasave.entity.UserRole;
import com.infrasave.repository.user.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author huseyinaydin
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  public CustomUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Optional<User> userOptional = userRepository.getByEmail(email);
    User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("Username not found."));
    List<UserRole> roles = user.getRoles();
    List<SimpleGrantedAuthority> simpleGrantedAuthorities =
        roles.stream().map(role -> new SimpleGrantedAuthority(role.getRole())).toList();
    return new CustomUserDetails(user.getId(), user.getEmail(), user.getPassword(), simpleGrantedAuthorities, true,
                                 true, true, true);
  }
}
