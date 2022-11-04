package com.infrasave.repository;

import com.infrasave.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author huseyinaydin
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

}
