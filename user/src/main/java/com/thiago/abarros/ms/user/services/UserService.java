package com.thiago.abarros.ms.user.services;

import com.thiago.abarros.ms.user.dtos.LoginRequestDTO;
import com.thiago.abarros.ms.user.dtos.RecoverRequestDTO;
import com.thiago.abarros.ms.user.dtos.ResponseDTO;
import com.thiago.abarros.ms.user.dtos.UserRecordDTO;
import com.thiago.abarros.ms.user.infra.security.TokenService;
import com.thiago.abarros.ms.user.models.User;
import com.thiago.abarros.ms.user.producers.UserProducer;
import com.thiago.abarros.ms.user.repository.UserRepository;
import com.thiago.abarros.ms.user.utils.PasswordGenerator;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

  final UserRepository userRepository;
  final UserProducer userProducer;
  final TokenService tokenService;
  final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, UserProducer userProducer,
      PasswordEncoder passwordEncoder, TokenService tokenService) {
    this.userRepository = userRepository;
    this.userProducer = userProducer;
    this.tokenService = tokenService;
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional
  public User registerUser(UserRecordDTO userDTO) {
    Optional<User> user = userRepository.findByEmail(userDTO.email());
    if (user.isEmpty()) {
      User newUser = new User();
      BeanUtils.copyProperties(userDTO, newUser);
      newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

      this.userRepository.save(newUser);
      this.userProducer.publishRegisterMessageEmail(newUser);
      return newUser;
    }
    return null;
  }

  public ResponseDTO loginUser(LoginRequestDTO loginRequest) {
    User user = userRepository.findByEmail(loginRequest.email())
        .orElseThrow(() -> new RuntimeException("User not found"));

    if (passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
      var token = tokenService.generateToken(user);
      return new ResponseDTO(user.getName(), token);
    }

    return null;
  }

  @Transactional
  public ResponseDTO forgotPassword(RecoverRequestDTO recoverRequestDTO) {

    User user = userRepository.findByEmail(recoverRequestDTO.email())
        .orElseThrow(() -> new RuntimeException("User not found"));

    if (user != null) {
      var newPassword = PasswordGenerator.generateRandomPassword(14);

      user.setPassword(passwordEncoder.encode(newPassword));
      userRepository.save(user);
      this.userProducer.publishRecoverPasswordMessageEmail(user, newPassword);

      return new ResponseDTO(user.getName(), newPassword);
    }
    return null;
  }
}
