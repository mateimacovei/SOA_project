package com.matei.users.repo;

import com.matei.users.model.Role;
import com.matei.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByUsernameNotAndRole(String username, Role role);
}
