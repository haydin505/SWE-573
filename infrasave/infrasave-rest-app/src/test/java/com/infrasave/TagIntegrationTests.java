package com.infrasave;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.infrasave.bean.AddContentRequest;
import com.infrasave.bean.AddTagRequest;
import com.infrasave.bean.LoginRequest;
import com.infrasave.entity.User;
import com.infrasave.entity.UserRole;
import com.infrasave.enums.VisibilityLevel;
import com.infrasave.repository.user.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author huseyinaydin
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(
    locations = "classpath:application-integrationtest.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class TagIntegrationTests {

  private static ObjectMapper MAPPER = JsonMapper.builder()
                                                 .findAndAddModules()
                                                 .build();

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UserRepository userRepository;

  @Test
  @Order(1)
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

  @Test
  @Order(2)
  public void shouldCreateTag() throws Exception {
    MockHttpSession session = getMockHttpSession();
    AddTagRequest req = new AddTagRequest("test-tag", "test-tag-description", "#color");
    String content = MAPPER.writeValueAsString(req);
    mockMvc.perform(post("/tags")
                        .contentType(MediaType.APPLICATION_JSON).content(content).session(session))
           .andExpect(status().isOk())
           .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("successful", is(true)));
  }

  @Test
  @Order(3)
  public void shouldCreateContent() throws Exception {
    MockHttpSession session = getMockHttpSession();
    AddContentRequest req =
        new AddContentRequest(VisibilityLevel.EVERYONE, "Test-title", "test-url", "test-imageUrl", "Test-description",
                              List.of(1L));
    String content = MAPPER.writeValueAsString(req);
    mockMvc.perform(post("/contents")
                        .contentType(MediaType.APPLICATION_JSON).session(session)
                        .content(content))
           .andExpect(status().isOk())
           .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("successful", is(true)));
  }

  @Test
  @Order(4)
  public void shouldGetMyFeedWithTag() throws Exception {
    MockHttpSession session = getMockHttpSession();
    mockMvc.perform(get("/contents/my-feed")
                        .contentType(MediaType.APPLICATION_JSON).session(session))
           .andExpect(status().isOk())
           .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("successful", is(true)))
           .andExpect(content().json("{\"successful\":true,\"errorCode\":null,\"errorTitle\":null,"
                                     + "\"errorDetail\":null,\"violations\":null,\"data\":[{\"id\":1,"
                                     + "\"visibilityLevel\":\"EVERYONE\",\"title\":\"Test-title\","
                                     + "\"url\":\"test-url\",\"imageUrl\":\"test-imageUrl\","
                                     + "\"description\":\"Test-description\",\"myContent\":false,"
                                     + "\"creatorUser\":{\"username\":\"mehmet600\",\"userId\":1,\"name\":\"Mehmet\","
                                     + "\"surname\":\"Selman\",\"email\":null,\"phoneNumber\":null,"
                                     + "\"birthDate\":null,\"createdContents\":null,\"roles\":null,"
                                     + "\"friendCount\":null,\"friends\":null,\"friendDTO\":null},"
                                     + "\"tags\":[{\"id\":1,\"name\":\"test-tag\","
                                     + "\"description\":\"test-tag-description\",\"color\":\"#color\"}]}]}"));
  }

  private MockHttpSession getMockHttpSession() throws Exception {
    LoginRequest request = new LoginRequest("infrasave2.app@gmail.com", "123456");
    String content = MAPPER.writeValueAsString(request);
    ResultActions resultActions = mockMvc.perform(
                                             post("/login").content(content)
                                                           .contentType(MediaType.APPLICATION_JSON))
                                         .andExpect(status().isOk())
                                         .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                         .andExpect(jsonPath("successful", is(true)));
    MvcResult mvcResult = resultActions.andReturn();
    MockHttpServletRequest mvcResultRequest = mvcResult.getRequest();
    return (MockHttpSession) mvcResultRequest.getSession(false);
  }
}
