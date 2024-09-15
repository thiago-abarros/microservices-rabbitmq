package com.thiago.abarros.ms.user.dtos;

import jakarta.validation.constraints.Email;

public record RecoverRequestDTO(@Email String email) {

}
