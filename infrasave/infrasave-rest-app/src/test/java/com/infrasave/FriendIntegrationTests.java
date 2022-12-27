package com.infrasave;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.infrasave.bean.AddFriendRequest;
import com.infrasave.bean.LoginRequest;
import com.infrasave.bean.UpdateFriendRequestStatusRequest;
import com.infrasave.entity.User;
import com.infrasave.entity.UserRole;
import com.infrasave.enums.FriendRequestStatus;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
public class FriendIntegrationTests {

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
    User user2 = new User();
    user2.setUsername("suleyman500");
    user2.setName("Süleyman");
    user2.setSurname("Kara");
    user2.setEmail("infrasave.app@gmail.com");
    user2.setPassword(passwordEncoder.encode("123456"));
    user2.setPhoneNumber("+905111111111");
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

    LoginRequest request = new LoginRequest("infrasave.app@gmail.com", "123456");
    String content = MAPPER.writeValueAsString(request);
    ResultActions resultActions = mockMvc.perform(
                                             post("/login").content(content).contentType(MediaType.APPLICATION_JSON))
                                         .andExpect(status().isOk())
                                         .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                         .andExpect(jsonPath("successful", is(true)));
    LoginRequest request2 = new LoginRequest("infrasave2.app@gmail.com", "123456");
    content = MAPPER.writeValueAsString(request2);
    mockMvc.perform(
               post("/login").content(content).contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("successful", is(true)));
  }

  @Test
  @Order(2)
  public void shouldSendFriendRequest() throws Exception {
    MockHttpSession session = getMockHttpSession();
    AddFriendRequest req = new AddFriendRequest(2L);
    String content = MAPPER.writeValueAsString(req);
    mockMvc.perform(post("/friends")
                        .contentType(MediaType.APPLICATION_JSON).content(content).session(session))
           .andExpect(status().isOk())
           .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("successful", is(true)));
  }

  @Test
  @Order(3)
  public void shouldGetPendingFriendRequest() throws Exception {
    MockHttpSession session = getMockHttpSessionForRequestee();
    mockMvc.perform(get("/friends/requests/requestee/pending")
                        .contentType(MediaType.APPLICATION_JSON).session(session))
           .andExpect(status().isOk())
           .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("successful", is(true)))
           .andExpect(content().json("{\"successful\":true,\"errorCode\":null,\"errorTitle\":null,"
                                     + "\"errorDetail\":null,\"violations\":null,\"data\":[{\"id\":1,"
                                     + "\"requesteeId\":2,\"requesterId\":1,\"friendRequestStatus\":\"PENDING\","
                                     + "\"requesterDetail\":{\"username\":\"mehmet600\",\"userId\":1,"
                                     + "\"name\":\"Mehmet\",\"surname\":\"Selman\",\"email\":null,"
                                     + "\"phoneNumber\":null,\"birthDate\":null,\"createdContents\":null,"
                                     + "\"roles\":null,\"friendCount\":null,\"friends\":null,\"friendDTO\":null}}]}"));
  }

  @Test
  @Order(4)
  public void shouldApprovePendingFriendRequest() throws Exception {
    MockHttpSession session = getMockHttpSessionForRequestee();
    UpdateFriendRequestStatusRequest req = new UpdateFriendRequestStatusRequest(1L, FriendRequestStatus.APPROVED);
    String content = MAPPER.writeValueAsString(req);
    mockMvc.perform(put("/friends")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .session(session))
           .andExpect(status().isOk())
           .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("successful", is(true)));
  }

  @Test
  @Order(5)
  public void shouldGetApprovedFriends() throws Exception {
    MockHttpSession session = getMockHttpSessionForRequestee();
    mockMvc.perform(get("/friends")
                        .contentType(MediaType.APPLICATION_JSON).session(session))
           .andExpect(status().isOk())
           .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("successful", is(true)))
           .andExpect(content().json("{\"successful\":true,\"errorCode\":null,\"errorTitle\":null,"
                                     + "\"errorDetail\":null,\"violations\":null,"
                                     + "\"data\":[{\"username\":\"mehmet600\",\"userId\":1,\"name\":\"Mehmet\","
                                     + "\"surname\":\"Selman\",\"email\":null,\"phoneNumber\":null,"
                                     + "\"birthDate\":null,\"createdContents\":null,\"roles\":null,"
                                     + "\"friendCount\":null,\"friends\":null,\"friendDTO\":null}]}"));
  }

  @Test
  @Order(6)
  public void shouldRemoveFriend() throws Exception {
    MockHttpSession session = getMockHttpSessionForRequestee();
    mockMvc.perform(delete("/friends" + "/1")
                        .contentType(MediaType.APPLICATION_JSON).session(session))
           .andExpect(status().isOk())
           .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("successful", is(true)));
  }

  @Test
  @Order(7)
  public void shouldGetEmptyApprovedFriends() throws Exception {
    MockHttpSession session = getMockHttpSessionForRequestee();
    mockMvc.perform(get("/friends")
                        .contentType(MediaType.APPLICATION_JSON).session(session))
           .andExpect(status().isOk())
           .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("successful", is(true)))
           .andExpect(content().json("{\"successful\":true,\"errorCode\":null,\"errorTitle\":null,"
                                     + "\"errorDetail\":null,\"violations\":null,\"data\":[]}"));
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

  private MockHttpSession getMockHttpSessionForRequestee() throws Exception {
    LoginRequest request = new LoginRequest("infrasave.app@gmail.com", "123456");
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
