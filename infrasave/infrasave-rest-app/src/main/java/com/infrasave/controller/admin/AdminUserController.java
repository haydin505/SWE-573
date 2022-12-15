package com.infrasave.controller.admin;

import com.infrasave.bean.UserDTO;
import com.infrasave.entity.User;
import com.infrasave.entity.UserRole;
import com.infrasave.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huseyinaydin
 */
@RestController
@RequestMapping("admin/users")
public class AdminUserController {

  private final UserService userService;

  public AdminUserController(UserService userService) {
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
}
