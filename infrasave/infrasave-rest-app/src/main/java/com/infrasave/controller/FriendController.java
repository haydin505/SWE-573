package com.infrasave.controller;

import com.infrasave.bean.AppResponse;
import com.infrasave.bean.AppResponses;
import com.infrasave.bean.UserDTO;
import com.infrasave.service.FriendService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huseyinaydin
 */
@RestController
@RequestMapping("friends")
public class FriendController {

  private final FriendService friendService;

  public FriendController(FriendService friendService) {
    this.friendService = friendService;
  }

  @GetMapping
  public AppResponse<List<UserDTO>> getFriends() {
    List<UserDTO> userDTOs = friendService.getCurrentUserFriendList().stream().map(UserDTO::new).toList();
    return AppResponses.successful(userDTOs);
  }

  //@GetMapping("requestee-pending")
  //public AppResponse<List<UserDTO>> getPendingFriends() {
  //  List<UserDTO> userDTOs = friendService.getRequesteePendingFriendList().stream().map(UserDTO::new).toList();
  //  return AppResponses.successful(userDTOs);
  //}
}
