package com.thiago.abarros.ms.email.dtos;

import java.util.UUID;

public record EmailRecordDTO(UUID userId, String emailTo, String subject, String text) {

}
