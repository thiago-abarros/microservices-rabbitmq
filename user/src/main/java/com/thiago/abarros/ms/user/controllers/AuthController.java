package com.thiago.abarros.ms.user.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.Authentication;

import com.thiago.abarros.ms.user.dtos.ChangePasswordRequestDTO;
import com.thiago.abarros.ms.user.dtos.LoginRequestDTO;
import com.thiago.abarros.ms.user.dtos.RecoverRequestDTO;
import com.thiago.abarros.ms.user.dtos.ResponseDTO;
import com.thiago.abarros.ms.user.dtos.UserRecordDTO;

import jakarta.validation.Valid;

/**
 * Interface for authentication-related operations.
 */
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication-related operations")
public interface AuthController {

    /**
     * Handles user registration.
     * 
     * @param userRecordDTO User registration data transfer object.
     * @return HTTP response with user registration result.
     */
    @Operation(summary = "Register a new user", description = "Creates a new user account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping("/register")
    ResponseEntity<ResponseDTO> register(@RequestBody @Valid UserRecordDTO userRecordDTO);

    /**
     * Handles user login.
     * 
     * @param loginRequestDTO User login data transfer object.
     * @return HTTP response with user login result.
     */
    @Operation(summary = "Login to an existing user account", description = "Authenticates a user and returns a token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User logged in successfully"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    ResponseEntity<ResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO);

    /**
     * Handles password change. This route requires authentication.
     * 
     * @param changePasswordRequestDTO Password change data transfer object.
     * @return HTTP response with password change result.
     */
    @Operation(summary = "Change the password of an existing user account", 
               description = "Updates the password of a user",
               security = { @SecurityRequirement(name = "Bearer Auth") })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Password changed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PutMapping("/change-password")
    ResponseEntity<String> changePassword(@RequestBody @Valid ChangePasswordRequestDTO changePasswordRequestDTO);

    /**
     * Handles password recovery.
     * 
     * @param recoverRequestDTO Password recovery data transfer object.
     * @return HTTP response with password recovery result.
     */
    @Operation(summary = "Recover the password of an existing user account", description = "Sends a password recovery email to the user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Password recovery email sent successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping("/forgot-password")
    ResponseEntity<String> forgotPassword(@RequestBody @Valid RecoverRequestDTO recoverRequestDTO);

    /**
     * Tests user authentication. This route requires authentication.
     * 
     * @param authentication User authentication object.
     * @return HTTP response with authentication result.
     */
    @Operation(summary = "Test user authentication", 
               description = "Verifies the authentication of a user",
               security = { @SecurityRequirement(name = "Bearer Auth") })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User authenticated successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/test-auth")
    ResponseEntity<String> authTest(@Parameter(hidden = true) Authentication authentication);
}
