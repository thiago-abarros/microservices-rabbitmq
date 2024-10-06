package com.thiago.abarros.ms.user.dtos;

import java.util.UUID;

public record RecoverPasswordDTO (UUID userId, String emailTo, String subject, String text, String token) {
    
}
