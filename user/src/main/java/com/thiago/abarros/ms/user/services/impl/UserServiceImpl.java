package com.thiago.abarros.ms.user.services.impl;

import com.thiago.abarros.ms.user.dtos.*;
import com.thiago.abarros.ms.user.infra.security.TokenService;
import com.thiago.abarros.ms.user.models.User;
import com.thiago.abarros.ms.user.producers.UserProducer;
import com.thiago.abarros.ms.user.repository.UserRepository;
import com.thiago.abarros.ms.user.services.UserService;
import com.thiago.abarros.ms.user.utils.PasswordGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    final UserRepository userRepository;
    final UserProducer userProducer;
    final TokenService tokenService;
    final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User registerUser(UserRecordDTO userDTO) {
        log.info("Registering new user with email {}", userDTO.email());
        Optional<User> user = this.userRepository.findByEmail(userDTO.email());

        if (user.isEmpty()) {
            log.debug("User not found, creating new user");
            User newUser = new User();
            BeanUtils.copyProperties(userDTO, newUser);
            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

            log.info("Saving new user to database");
            this.userRepository.save(newUser);

            log.info("Publishing register message to email queue");
            this.userProducer.publishRegisterMessageEmail(newUser);
            log.info("User registered successfully");
        } else {
            log.warn("User already exists with email {}", userDTO.email());
        }
        return user.orElseThrow();
    }

    @Override
    public ResponseDTO loginUser(LoginRequestDTO loginRequest) {
        log.info("Logging in user with email {}", loginRequest.email());
        User user = this.userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            log.info("User authenticated successfully");
            var token = tokenService.generateToken(user);
            log.info("Generated token for user {}", user.getName());
            return new ResponseDTO(user.getName(), token);
        } else {
            log.warn("Invalid credentials for user {}", loginRequest.email());
        }
        return null;
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordRequestDTO changePasswordRequest) {
        log.info("Changing password for user with email {}", changePasswordRequest.email());
        User user = this.userRepository.findByEmail(changePasswordRequest.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (passwordEncoder.matches(changePasswordRequest.oldPassword(), user.getPassword())) {
            log.info("Old password matches, updating password");
            user.setPassword(passwordEncoder.encode(changePasswordRequest.newPassword()));
            this.userRepository.save(user);
            log.info("Password updated successfully for user {}", user.getName());
        } else {
            log.warn("Old password does not match for user {}", changePasswordRequest.email());
            throw new RuntimeException("Old password does not match");
        }
    }

    @Override
    @Transactional
    public ResponseDTO forgotPassword(RecoverRequestDTO recoverRequestDTO) {
        log.info("Recovering password for user with email {}", recoverRequestDTO.email());
        User user = this.userRepository.findByEmail(recoverRequestDTO.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user != null) {
            log.info("Generating new password for user {}", user.getName());
            var oldPassword = user.getPassword();
            var newPassword = PasswordGenerator.generateRandomPassword(14);

            user.setPassword(passwordEncoder.encode(newPassword));
            this.userRepository.save(user);
            log.info("Password updated successfully for user {}", user.getName());
            this.userProducer.publishRecoverPasswordMessageEmail(user, newPassword);
            log.info("Recover password message sent to user {}", user.getName());

            this.schedulePasswordRevert(user, oldPassword, newPassword);
            log.info("Scheduled password revert for user {}", user.getName());

            return new ResponseDTO(user.getName(), newPassword);
        }
        return null;
    }

    private void schedulePasswordRevert(User user, String oldPassword, String newPassword) {
        log.info("Scheduling password revert for user {}", user.getName());
        long expirationTimeMillis = 10; // 10 minutes expiration time

        Executors.newSingleThreadScheduledExecutor().schedule(() -> {
            log.info("Reverting password for user {}", user.getName());
            User currentUser = userRepository.findById(user.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (passwordEncoder.matches(newPassword, currentUser.getPassword())) {
                log.info("Reverting password to old password for user {}", user.getName());
                currentUser.setPassword(oldPassword);
                userRepository.save(currentUser);
                log.info("Password reverted successfully for user {}", user.getName());
            }
        }, expirationTimeMillis, TimeUnit.MINUTES);
    }
}
