package com.infrasave;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.infrasave.bean.LoginRequest;
import com.infrasave.bean.RegisterRequest;
import com.infrasave.entity.User;
import com.infrasave.entity.UserRole;
import com.infrasave.repository.user.UserRepository;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(
    locations = "classpath:application-integrationtest.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class AccountIntegrationTests {

  private static ObjectMapper MAPPER = JsonMapper.builder()
                                                 .findAndAddModules()
                                                 .build();

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UserRepository userRepository;

  @Order(1)
  @Test
  public void shouldLogin() throws Exception {
    User user = new User();
    user.setUsername("mehmet600");
    user.setName("Mehmet");
    user.setSurname("Selman");
    user.setEmail("infrasave2.app@gmail.com");
    user.setPassword(passwordEncoder.encode("123456"));
    user.setPhoneNumber("+905111111111");
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
    //
    LoginRequest request = new LoginRequest("infrasave2.app@gmail.com", "123456");
    String content = MAPPER.writeValueAsString(request);
    ResultActions resultActions = mockMvc.perform(
                                             post("/login").content(content).contentType(MediaType.APPLICATION_JSON))
                                         .andExpect(status().isOk())
                                         .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                         .andExpect(jsonPath("successful", is(true)));
  }

  @Order(2)
  @Test
  public void shouldFailLogin() throws Exception {
    User user = new User();
    user.setUsername("mehmet601");
    user.setName("Mehmet");
    user.setSurname("Selman");
    user.setEmail("infrasave3.app@gmail.com");
    user.setPassword(passwordEncoder.encode("123456"));
    user.setPhoneNumber("+905111111111");
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
    //
    LoginRequest request = new LoginRequest("infrasave3.app@gmail.com", "invalidPassword");
    String content = MAPPER.writeValueAsString(request);
    ResultActions resultActions = mockMvc.perform(
                                             post("/login").content(content).contentType(MediaType.APPLICATION_JSON))
                                         .andExpect(status().isOk())
                                         .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                         .andExpect(jsonPath("successful", is(false)))
                                         .andExpect(jsonPath("errorCode", is("5000")));
  }

  @Order(3)
  @Test
  public void shouldRegister() throws Exception {
    RegisterRequest request =
        new RegisterRequest("Hüseyin", "Aydın", "haydin505", "infrasave.app@gmail.com", "123456", "+90511111111",
                            LocalDateTime.now().minus(1L, ChronoUnit.DAYS));
    String content = MAPPER.writeValueAsString(request);
    ResultActions resultActions = mockMvc.perform(
                                             post("/register").content(content).contentType(MediaType.APPLICATION_JSON))
                                         .andExpect(status().isOk())
                                         .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                         .andExpect(jsonPath("successful", is(true)));
  }

  @Order(4)
  @Test
  public void shouldFailRegister() throws Exception {
    RegisterRequest request =
        new RegisterRequest("Hüseyin", "Aydın", "", "infrasave.app@gmail.com", "123456", "+90511111111",
                            LocalDateTime.now().minus(1L, ChronoUnit.DAYS));
    String content = MAPPER.writeValueAsString(request);
    ResultActions resultActions = mockMvc.perform(
                                             post("/register").content(content).contentType(MediaType.APPLICATION_JSON))
                                         .andExpect(status().isBadRequest());
  }
}
