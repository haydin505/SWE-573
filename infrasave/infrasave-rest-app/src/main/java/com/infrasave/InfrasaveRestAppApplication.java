package com.infrasave;

import com.infrasave.entity.Content;
import com.infrasave.entity.Friend;
import com.infrasave.entity.Tag;
import com.infrasave.entity.User;
import com.infrasave.entity.UserRole;
import com.infrasave.enums.FriendRequestStatus;
import com.infrasave.enums.VisibilityLevel;
import com.infrasave.repository.content.ContentRepository;
import com.infrasave.repository.friend.FriendRepository;
import com.infrasave.repository.tag.TagRepository;
import com.infrasave.repository.user.UserRepository;
import com.infrasave.service.AccountService;
import com.infrasave.service.EmailService;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@SpringBootApplication
@EnableJpaRepositories
@EnableSwagger2
public class InfrasaveRestAppApplication {

  public static void main(String[] args) {
    SpringApplication.run(InfrasaveRestAppApplication.class, args);
  }

  @Bean
  @Transactional
  @ConditionalOnProperty(value = "spring.profiles.active",
                         havingValue = "dev",
                         matchIfMissing = false)
  CommandLineRunner commandLineRunner(EmailService emailService,
                                      AccountService accountService,
                                      UserRepository userRepository,
                                      ContentRepository contentRepository,
                                      FriendRepository friendRepository,
                                      TagRepository tagRepository,
                                      PasswordEncoder passwordEncoder) {
    return args -> {
      User user = new User();
      user.setUsername("mehmet600");
      user.setName("Mehmet");
      user.setSurname("Selman");
      user.setEmail("mhnaydin505@gmail.com");
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

      Tag financeTag = new Tag();
      financeTag.setName("finance");
      financeTag.setDescription("Finance related contents");
      financeTag.setColor("#227C70");
      financeTag.setCreatedAt(now);
      financeTag.setLastUpdatedAt(now);
      tagRepository.save(financeTag);

      Content content = new Content();
      content.setCreatedAt(now);
      content.setLastUpdatedAt(now);
      content.setTitle("Analysis: Nagging U.S. Treasury liquidity problems raise Fed balance sheet predicament");
      content.setUrl("https://twitter.com/Reuters/status/1590027914367713280");
      content.setImageUrl("https://pbs.twimg.com/media/FhDp9jIXEAIllKD?format=jpg&name=medium");
      content.setCreatorId(user);
      content.setVisibilityLevel(VisibilityLevel.EVERYONE);
      content.setTags(new HashSet<>(List.of(financeTag)));
      contentRepository.save(content);

      User user2 = new User();
      user2.setUsername("suleyman123");
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

      Tag defaultTag = new Tag();
      defaultTag.setName("default");
      defaultTag.setDescription("default tag");
      defaultTag.setColor("#6B728E");
      defaultTag.setCreatedAt(now);
      defaultTag.setLastUpdatedAt(now);
      tagRepository.save(defaultTag);

      Content friendContent = new Content();
      friendContent.setCreatedAt(now);
      friendContent.setLastUpdatedAt(now);
      friendContent.setTitle("Test-Title");
      friendContent.setUrl("Test-Url");
      friendContent.setCreatorId(user2);
      friendContent.setVisibilityLevel(VisibilityLevel.FRIENDS);
      friendContent.setTags(new HashSet<>(List.of(financeTag, defaultTag)));
      contentRepository.save(friendContent);

      emailService.sendSimpleMessage("mhnaydin505@gmail.com", "Application Started Successfully!",
                                     "Hello, your application started successfully!");
      //accountService.resetPassword("mhnaydin505@gmail.com");
    };
  }
}
