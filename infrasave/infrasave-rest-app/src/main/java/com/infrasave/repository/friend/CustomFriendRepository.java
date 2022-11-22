package com.infrasave.repository.friend;

import com.infrasave.entity.Friend;
import com.infrasave.entity.User;
import java.util.List;

/**
 * @author huseyinaydin
 */
public interface CustomFriendRepository {

  List<Friend> getFriendListByUser(User user);
}
