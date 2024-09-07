package com.thiago.abarros.ms.user.services;

import com.thiago.abarros.ms.user.models.UserModel;
import com.thiago.abarros.ms.user.producers.UserProducer;
import com.thiago.abarros.ms.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

  final UserRepository userRepository;
  final UserProducer userProducer;

  public UserService(UserRepository userRepository, UserProducer userProducer) {
    this.userRepository = userRepository;
    this.userProducer = userProducer;
  }

  @Transactional
  public UserModel saveUser(UserModel userModel) {
    userModel = userRepository.save(userModel);
    userProducer.publishMessageEmail(userModel);
    return userModel;
  }
}
