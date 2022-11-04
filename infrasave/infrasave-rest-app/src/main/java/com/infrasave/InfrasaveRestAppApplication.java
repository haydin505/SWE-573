package com.infrasave;

import com.infrasave.entity.User;
import com.infrasave.entity.UserRole;
import com.infrasave.repository.UserRepository;
import com.infrasave.repository.UserRoleRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@SpringBootApplication
@EnableJpaRepositories
public class InfrasaveRestAppApplication {

  public static void main(String[] args) {
    SpringApplication.run(InfrasaveRestAppApplication.class, args);
  }

  @Bean
  CommandLineRunner commandLineRunner(UserRepository userRepository,
                                      UserRoleRepository userRoleRepository,
                                      PasswordEncoder passwordEncoder) {
    return args -> {
      User user = new User();
      user.setName("Suleyman");
      user.setSurname("Yar");
      user.setEmail("test@test.com");
      user.setPassword(passwordEncoder.encode("123456"));
      LocalDateTime now = LocalDateTime.now();
      user.setBirthDate(now);
      user.setLastUpdatedAt(now);
      user.setCreatedAt(now);

      UserRole userRole = new UserRole();
      userRole.setRole("ROLE_USER");
      userRole.setUser(user);
      userRole.setCreatedAt(now);
      userRole.setLastUpdatedAt(now);
      user.setRoles(List.of(userRole));
      userRepository.save(user);
    };
  }
}
