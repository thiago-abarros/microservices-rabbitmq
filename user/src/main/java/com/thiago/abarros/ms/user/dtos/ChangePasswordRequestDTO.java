package com.thiago.abarros.ms.user.dtos;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequestDTO(
    @NotBlank String email,
    @NotBlank String oldPassword,
    @NotBlank String newPassword) {
    
}
