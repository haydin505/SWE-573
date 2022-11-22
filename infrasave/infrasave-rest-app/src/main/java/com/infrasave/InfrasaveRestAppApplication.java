package com.infrasave;

import com.infrasave.entity.Content;
import com.infrasave.entity.Friend;
import com.infrasave.entity.User;
import com.infrasave.entity.UserRole;
import com.infrasave.enums.FriendRequestStatus;
import com.infrasave.enums.VisibilityLevel;
import com.infrasave.repository.content.ContentRepository;
import com.infrasave.repository.friend.FriendRepository;
import com.infrasave.repository.user.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@SpringBootApplication
@EnableJpaRepositories
public class InfrasaveRestAppApplication {

  public static void main(String[] args) {
    SpringApplication.run(InfrasaveRestAppApplication.class, args);
  }

  @Bean
  @Transactional
  CommandLineRunner commandLineRunner(UserRepository userRepository,
                                      ContentRepository contentRepository,
                                      FriendRepository friendRepository,
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

      Content content = new Content();
      content.setCreatedAt(now);
      content.setLastUpdatedAt(now);
      content.setTitle("Test-Title");
      content.setUrl("Test-Url");
      content.setCreatorId(user);
      content.setVisibilityLevel(VisibilityLevel.EVERYONE);
      contentRepository.save(content);

      User user2 = new User();
      user2.setName("Suleyman");
      user2.setSurname("Yar");
      user2.setEmail("test@test2.com");
      user2.setPassword(passwordEncoder.encode("123456"));
      user2.setBirthDate(now);
      user2.setLastUpdatedAt(now);
      user2.setCreatedAt(now);

      UserRole userRole2 = new UserRole();
      userRole2.setRole("ROLE_USER");
      userRole2.setUser(user);
      userRole2.setCreatedAt(now);
      userRole2.setLastUpdatedAt(now);
      user2.setRoles(List.of(userRole2));
      userRepository.save(user2);

      Friend friend = new Friend();
      friend.setRequester(user2);
      friend.setRequestee(user);
      friend.setStatus(FriendRequestStatus.APPROVED);
      friend.setCreatedAt(now);
      friend.setLastUpdatedAt(now);
      friendRepository.save(friend);

      Content friendContent = new Content();
      friendContent.setCreatedAt(now);
      friendContent.setLastUpdatedAt(now);
      friendContent.setTitle("Test-Title");
      friendContent.setUrl("Test-Url");
      friendContent.setCreatorId(user2);
      friendContent.setVisibilityLevel(VisibilityLevel.FRIENDS);
      contentRepository.save(friendContent);
    };
  }
}
