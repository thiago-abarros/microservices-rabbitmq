package com.thiago.abarros.ms.user.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRecordDTO(@NotBlank String name, @NotBlank @Email String email,
                            @NotBlank String password) {

}
