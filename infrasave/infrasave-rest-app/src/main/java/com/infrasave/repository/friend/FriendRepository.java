package com.infrasave.repository.friend;

import com.infrasave.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author huseyinaydin
 */
public interface FriendRepository extends JpaRepository<Friend, Long>, CustomFriendRepository {

}
