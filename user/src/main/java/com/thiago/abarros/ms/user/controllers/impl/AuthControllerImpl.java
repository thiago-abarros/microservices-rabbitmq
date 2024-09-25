package com.thiago.abarros.ms.user.controllers.impl;

import com.thiago.abarros.ms.user.controllers.AuthController;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthControllerImpl implements AuthController {

  final UserService userService;
  final TokenService tokenService;

  public AuthControllerImpl(UserService userService, TokenService tokenService) {
    this.userService = userService;
    this.tokenService = tokenService;
  }

  @Override
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

  @Override
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

  @Override
  public ResponseEntity<String> changePassword(@RequestBody @Valid ChangePasswordRequestDTO changePasswordRequestDTO) {
    this.userService.changePassword(changePasswordRequestDTO);

    return ResponseEntity.status(HttpStatus.OK).body("Password changed!");
  }

  @Override
  public ResponseEntity<String> forgotPassword(@RequestBody @Valid RecoverRequestDTO recoverRequestDTO) {

    this.userService.forgotPassword(recoverRequestDTO);
    return ResponseEntity.status(HttpStatus.OK).body("Password recovered!");
  }

  @Override
  public ResponseEntity<String> authTest(Authentication authentication) {

    return ResponseEntity.status(HttpStatus.OK)
        .body("User Authenticated " + authentication.getDetails());
  }
}
