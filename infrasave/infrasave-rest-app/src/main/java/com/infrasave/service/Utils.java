package com.infrasave.service;

import com.infrasave.bean.UserDTO;
import com.infrasave.entity.User;

/**
 * @author huseyinaydin
 */
public class Utils {

  public static UserDTO mapCreatorToUserDTO(User user) {
    return new UserDTO(user.getUsername(), user.getId(), user.getName(), user.getSurname(), null, null, null, null,
                       null);
  }
}
