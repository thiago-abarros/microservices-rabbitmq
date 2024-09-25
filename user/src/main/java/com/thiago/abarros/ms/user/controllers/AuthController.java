package com.thiago.abarros.ms.user.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thiago.abarros.ms.user.dtos.ChangePasswordRequestDTO;
import com.thiago.abarros.ms.user.dtos.LoginRequestDTO;
import com.thiago.abarros.ms.user.dtos.RecoverRequestDTO;
import com.thiago.abarros.ms.user.dtos.ResponseDTO;
import com.thiago.abarros.ms.user.dtos.UserRecordDTO;

import io.swagger.oas.annotations.Operation;
import io.swagger.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RequestMapping("/auth")
public interface AuthController {

    @PostMapping(
        path = "/register",
        produces = "application/json"
    )
    @Operation(summary = "Register new user")
    @ApiResponse(
        responseCode = "201",
        description = "User created!"
    )
    public ResponseEntity<ResponseDTO> register(@RequestBody @Valid UserRecordDTO userRecordDTO);

    @PostMapping(
        path = "/login",
        produces = "application/json"
    )
    @Operation(summary = "Login user")
    public ResponseEntity<ResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO);
    
    @PostMapping(
        path = "/change-password",
        produces = "application/json"
    )
    @Operation(summary = "Change user password")
    public ResponseEntity<String> changePassword(@RequestBody @Valid ChangePasswordRequestDTO changePasswordRequestDTO);
    
    @PostMapping(
        path = "/forgot-password",
        produces = "application/json"
    )
    @Operation(summary = "Reset user password, send new password to email")
    public ResponseEntity<String> forgotPassword(@RequestBody @Valid RecoverRequestDTO recoverRequestDTO);

    @GetMapping(
        path = "/",
        produces = "application/json"
    )
    @Operation(summary = "Auth test")
    public ResponseEntity<String> authTest(Authentication authentication);
}
