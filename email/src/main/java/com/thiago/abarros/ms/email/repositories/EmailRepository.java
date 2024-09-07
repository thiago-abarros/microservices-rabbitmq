package com.thiago.abarros.ms.email.repositories;

import com.thiago.abarros.ms.email.models.EmailModel;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<EmailModel, UUID> {

}
