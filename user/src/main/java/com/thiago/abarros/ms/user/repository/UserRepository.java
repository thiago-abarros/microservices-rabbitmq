package com.thiago.abarros.ms.user.repository;

import com.thiago.abarros.ms.user.models.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {

  Optional<User> findByEmail(String email);
}
