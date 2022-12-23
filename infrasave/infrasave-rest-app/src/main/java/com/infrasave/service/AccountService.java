package com.infrasave.service;

import com.infrasave.bean.RegisterRequest;
import com.infrasave.entity.User;
import com.infrasave.entity.UserRole;
import com.infrasave.exception.ResetPasswordTokenExpiredException;
import com.infrasave.exception.UserAlreadyRegisteredException;
import com.infrasave.exception.UserNotFoundException;
import com.infrasave.exception.UsernameAlreadyTakenException;
import com.infrasave.repository.user.UserRepository;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author huseyinaydin
 */
@Service
public class AccountService {

  private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  private final EmailService emailService;

  public AccountService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.emailService = emailService;
  }

  public void register(RegisterRequest request) throws UserAlreadyRegisteredException {
    Optional<User> userOptional = userRepository.getByEmail(request.email());
    if (userOptional.isPresent()) {
      throw new UserAlreadyRegisteredException();
    }
    Optional<User> userByUsernameOptional = userRepository.getByUsername(request.username());
    if (userByUsernameOptional.isPresent()) {
      throw new UsernameAlreadyTakenException();
    }
    User user = new User();
    LocalDateTime now = LocalDateTime.now();
    user.setCreatedAt(now);
    user.setLastUpdatedAt(now);
    user.setPassword(passwordEncoder.encode(request.password()));
    user.setName(request.name());
    user.setSurname(request.surname());
    user.setUsername(request.username());
    user.setEmail(request.email());
    user.setBirthDate(request.birthDate());
    user.setPhoneNumber(request.phoneNumber());
    UserRole userRole = new UserRole();
    userRole.setCreatedAt(now);
    userRole.setLastUpdatedAt(now);
    userRole.setUser(user);
    userRole.setRole("ROLE_USER");
    user.setRoles(List.of(userRole));
    userRepository.save(user);
    String text = String.format("Hello %s %s,\n"
                                + "Thank you for registering. We wish you a good time in Infrasave.\n"
                                + "If you would like to contact us you can directly reply to this mail.",
                                user.getName(), user.getSurname());
    emailService.sendSimpleMessage(user.getEmail(), "Welcome to Infrasave", text);
  }

  public void resetPassword(String email) {
    Optional<User> userOptional = userRepository.getByEmail(email);
    if (userOptional.isEmpty()) {
      throw new UserNotFoundException();
    }
    User user = userOptional.get();
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime expireDate = now.plus(8, ChronoUnit.HOURS);
    user.setLastUpdatedAt(now);
    user.setResetPasswordTokenExpireDate(expireDate);
    String resetPasswordToken = UUID.randomUUID().toString();
    user.setResetPasswordToken(resetPasswordToken);

    HttpServletRequest request =
        ((ServletRequestAttributes
            ) RequestContextHolder.getRequestAttributes())
            .getRequest();
    String protocol = request.getProtocol();
    String requestURL = request.getRequestURL().toString();
    String requestURI = request.getRequestURI();
    LOGGER.info("com.infrasave.service.AccountService.resetPassword. Protocol: " + protocol);
    LOGGER.info("com.infrasave.service.AccountService.resetPassword. RequestURL: " + requestURL);
    LOGGER.info("com.infrasave.service.AccountService.resetPassword. RequestURI: " + requestURI);
    LOGGER.info("com.infrasave.service.AccountService.resetPassword. request: " + request);
    String text = String.format("Hello,\nIn order to reset your password please click link below.\n%s",
                                requestURL + "?token=" + resetPasswordToken);
    emailService.sendSimpleMessage(user.getEmail(), "Reset Password", text);
    userRepository.save(user);
  }

  public void resetPasswordWithToken(String token, String password) {
    Optional<User> userOptional = userRepository.getByResetPasswordToken(token);
    if (userOptional.isEmpty()) {
      throw new UserNotFoundException();
    }
    User user = userOptional.get();
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime expireDate = user.getResetPasswordTokenExpireDate();
    if (now.isAfter(expireDate)) {
      throw new ResetPasswordTokenExpiredException();
    }
    user.setLastUpdatedAt(now);
    user.setPassword(passwordEncoder.encode(password));
    user.setResetPasswordTokenExpireDate(null);
    user.setResetPasswordToken(null);
    userRepository.save(user);
  }
}
