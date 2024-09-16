package com.thiago.abarros.ms.user.controllers;

import com.thiago.abarros.ms.user.dtos.ChangePasswordRequestDTO;
import com.thiago.abarros.ms.user.dtos.LoginRequestDTO;
import com.thiago.abarros.ms.user.dtos.RecoverRequestDTO;
import com.thiago.abarros.ms.user.dtos.ResponseDTO;
import com.thiago.abarros.ms.user.dtos.UserRecordDTO;
import com.thiago.abarros.ms.user.infra.security.TokenService;
import com.thiago.abarros.ms.user.models.User;
import com.thiago.abarros.ms.user.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

  final UserService userService;
  final TokenService tokenService;

  public AuthController(UserService userService, TokenService tokenService) {
    this.userService = userService;
    this.tokenService = tokenService;
  }

  @PostMapping("/register")
  public ResponseEntity<ResponseDTO> register(@RequestBody @Valid UserRecordDTO userRecordDTO) {
    User user = this.userService.registerUser(userRecordDTO);

    if (user != null) {
      var token = tokenService.generateToken(user);

      return ResponseEntity
          .status(HttpStatus.CREATED)
          .body(new ResponseDTO("User " + user.getName() + " created!", token));
    }
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(new ResponseDTO("User already exists", null));
  }

  @PostMapping("/login")
  public ResponseEntity<ResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
    ResponseDTO response = this.userService.loginUser(loginRequestDTO);

    if (response != null) {
      return ResponseEntity
          .status(HttpStatus.OK)
          .body(new ResponseDTO(
              "User " + response.name() + " logged!",
              response.token()));
    }
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(new ResponseDTO("User not found", null));
  }

  @PostMapping("/change-password")
  public ResponseEntity<String> changePassword(@RequestBody @Valid ChangePasswordRequestDTO changePasswordRequestDTO) {
    this.userService.changePassword(changePasswordRequestDTO);

    return ResponseEntity.status(HttpStatus.OK).body("Password changed!");
  }

  @PostMapping("/forgot-password")
  public ResponseEntity<String> forgotPassword(@RequestBody @Valid RecoverRequestDTO recoverRequestDTO) {

    this.userService.forgotPassword(recoverRequestDTO);
    return ResponseEntity.status(HttpStatus.OK).body("Password recovered!");
  }

  @GetMapping("/")
  public ResponseEntity<String> authTest(Authentication authentication) {

    return ResponseEntity.status(HttpStatus.OK)
        .body("User Authenticated " + authentication.getDetails());
  }
}
