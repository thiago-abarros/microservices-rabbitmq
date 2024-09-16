package com.thiago.abarros.ms.user.services;

import com.rabbitmq.client.RpcClient.Response;
import com.thiago.abarros.ms.user.dtos.ChangePasswordRequestDTO;
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
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
  public void changePassword(ChangePasswordRequestDTO changePasswordRequest) {
    User user = userRepository.findByEmail(changePasswordRequest.email())
        .orElseThrow(() -> new RuntimeException("User not found"));

    if (passwordEncoder.matches(changePasswordRequest.oldPassword(), user.getPassword())) {
      user.setPassword(passwordEncoder.encode(changePasswordRequest.newPassword()));
      this.userRepository.save(user);
    } else {
      throw new RuntimeException("Old password does not match");
    }
  }

  @Transactional
  public ResponseDTO forgotPassword(RecoverRequestDTO recoverRequestDTO) {
    User user = userRepository.findByEmail(recoverRequestDTO.email())
        .orElseThrow(() -> new RuntimeException("User not found"));

    if (user != null) {
      var oldPassword = user.getPassword();
      var newPassword = PasswordGenerator.generateRandomPassword(14);

      user.setPassword(passwordEncoder.encode(newPassword));
      this.userRepository.save(user);
      this.userProducer.publishRecoverPasswordMessageEmail(user, newPassword);
      
      this.schedulePasswordRevert(user, oldPassword, newPassword);      
      
      return new ResponseDTO(user.getName(), newPassword);
    }
    return null;
  }

  private void schedulePasswordRevert(User user, String oldPassword, String newPassword) {
    long expirationTimeMillis = 10; // 10 minutes expiration time

    Executors.newSingleThreadScheduledExecutor().schedule(() -> {
        User currentUser = userRepository.findById(user.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (passwordEncoder.matches(newPassword, currentUser.getPassword())) {
            currentUser.setPassword(oldPassword);
            userRepository.save(currentUser);
        }
    }, expirationTimeMillis, TimeUnit.MINUTES);
  }
}
