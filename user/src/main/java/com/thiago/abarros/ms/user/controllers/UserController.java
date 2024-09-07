package com.thiago.abarros.ms.user.controllers;

import com.thiago.abarros.ms.user.dtos.UserRecordDTO;
import com.thiago.abarros.ms.user.models.UserModel;
import com.thiago.abarros.ms.user.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
  final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/users")
  public ResponseEntity<UserModel> saveUser(@RequestBody @Valid UserRecordDTO userRecordDTO) {
    var userModel = new UserModel();
    BeanUtils.copyProperties(userRecordDTO, userModel);

    return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(userModel));
  }
}
