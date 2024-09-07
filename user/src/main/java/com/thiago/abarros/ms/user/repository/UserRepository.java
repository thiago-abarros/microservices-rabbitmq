package com.thiago.abarros.ms.user.repository;

import com.thiago.abarros.ms.user.models.UserModel;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel, UUID> {

}
