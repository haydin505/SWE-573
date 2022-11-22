package com.infrasave.repository.user;

import com.infrasave.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author huseyinaydin
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository {
}
