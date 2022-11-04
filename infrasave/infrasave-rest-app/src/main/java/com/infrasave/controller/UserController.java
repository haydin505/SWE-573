package com.infrasave.controller;

import com.infrasave.bean.UserDTO;
import com.infrasave.config.CustomUserDetails;
import com.infrasave.entity.User;
import com.infrasave.entity.UserRole;
import com.infrasave.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huseyinaydin
 */
@RestController
@RequestMapping("users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @Secured({"ROLE_ADMIN"})
  @GetMapping("/{id}")
  public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
    User user = userService.getUserById(id);
    UserDTO userDTO = new UserDTO();
    userDTO.setName(user.getName());
    userDTO.setSurname(userDTO.getSurname());
    userDTO.setEmail(userDTO.getEmail());
    userDTO.setRoles(user.getRoles().stream().map(UserRole::getRole).toList());
    return ResponseEntity.ok(userDTO);
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
    return ResponseEntity.ok(userDTO);
  }
}
