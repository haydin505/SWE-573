package com.infrasave.controller;

import com.infrasave.bean.AppResponse;
import com.infrasave.bean.AppResponses;
import com.infrasave.bean.ContentDTO;
import com.infrasave.bean.TagDTO;
import com.infrasave.bean.UpdateUserRequest;
import com.infrasave.bean.UserDTO;
import com.infrasave.config.CustomUserDetails;
import com.infrasave.entity.User;
import com.infrasave.entity.UserRole;
import com.infrasave.service.FriendService;
import com.infrasave.service.UserService;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huseyinaydin
 */
@RestController
@RequestMapping("users")
public class UserController {

  private final UserService userService;

  private final FriendService friendService;

  public UserController(UserService userService, FriendService friendService, FriendService friendService1) {
    this.userService = userService;
    this.friendService = friendService1;
  }

  @GetMapping("/{id}")
  @ApiOperation("Get user page information")
  public AppResponse<UserDTO> getUser(@PathVariable Long id) {
    return AppResponses.successful(userService.getUserDTO(id));
  }

  @GetMapping("/current")
  public ResponseEntity<UserDTO> getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication instanceof AnonymousAuthenticationToken) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
    Long userId = userDetails.getUserId();
    User user = userService.getUserById(userId);
    UserDTO userDTO = new UserDTO();
    userDTO.setName(user.getName());
    userDTO.setSurname(user.getSurname());
    userDTO.setEmail(user.getEmail());
    userDTO.setRoles(user.getRoles().stream().map(UserRole::getRole).toList());
    userDTO.setUserId(user.getId());
    userDTO.setUsername(user.getUsername());
    List<Long> myContentIds = user.getMyContents().stream().map(myContent -> myContent.getContent().getId()).toList();
    userDTO.setCreatedContents(user.getCreatedContents().stream().map(content -> {
      List<TagDTO> tags = content.getTags().stream().map(TagDTO::new).toList();
      return new ContentDTO(content, myContentIds.contains(content.getId()), new UserDTO(user), tags);
    }).toList());
    List<User> friends = friendService.getApprovedFriendList(user);
    userDTO.setFriends(friends.stream().map(UserDTO::new).toList());
    userDTO.setFriendCount(friends.size());
    return ResponseEntity.ok(userDTO);
  }

  @PutMapping
  public AppResponse updateUser(@RequestBody UpdateUserRequest updateUserRequest) {
    CustomUserDetails principal =
        (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    userService.updateUser(principal.getUserId(),
                           updateUserRequest.name(),
                           updateUserRequest.surname(),
                           updateUserRequest.username(),
                           updateUserRequest.email(),
                           updateUserRequest.birthDate());
    return AppResponses.successful();
  }
}
