package com.infrasave.controller;

import com.infrasave.bean.AddFriendRequest;
import com.infrasave.bean.AppResponse;
import com.infrasave.bean.AppResponses;
import com.infrasave.bean.FriendDTO;
import com.infrasave.bean.UpdateFriendRequestStatusRequest;
import com.infrasave.bean.UserDTO;
import com.infrasave.entity.Friend;
import com.infrasave.service.FriendService;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

  @GetMapping("/requests/requestee/pending")
  public AppResponse<List<FriendDTO>> getRequesteePendingFriendRequests() {
    List<Friend> friendList = friendService.getRequesteePendingFriendRequests();
    List<FriendDTO> friendDTOs = friendList.stream()
                                           .map(friend -> new FriendDTO(friend.getId(),
                                                                        friend.getRequestee().getId(),
                                                                        friend.getRequester().getId(),
                                                                        friend.getStatus(),
                                                                        new UserDTO(friend.getRequester())))
                                           .toList();
    return AppResponses.successful(friendDTOs);
  }

  @PostMapping
  public AppResponse addFriend(@RequestBody @Validated AddFriendRequest request) {
    friendService.addFriend(request.requesteeId());
    return AppResponses.successful();
  }

  @PutMapping("/requests")
  public AppResponse updateFriendRequestStatus(@RequestBody @Validated UpdateFriendRequestStatusRequest request) {
    friendService.updateFriendRequestStatus(request.id(), request.friendRequestStatus());
    return AppResponses.successful();
  }

  @DeleteMapping("/{id}")
  public AppResponse deleteFriend(@PathVariable Long id) {
    friendService.deleteFriend(id);
    return AppResponses.successful();
  }
}
