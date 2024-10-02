package com.thiago.abarros.ms.user.services;

import com.thiago.abarros.ms.user.dtos.*;
import com.thiago.abarros.ms.user.models.User;

public interface UserService {

    /**
     * Registers a new user with the provided user record.
     *
     * @param userDTO the user record to register
     * @return the registered user
     * @throws RuntimeException if the user already exists
     */
    User registerUser(UserRecordDTO userDTO);

    /**
     * Authenticates a user with the provided login request.
     *
     * @param loginRequestDTO the login request to authenticate
     * @return a response containing the user's name and token if authentication is successful, or null otherwise
     */
    ResponseDTO loginUser(LoginRequestDTO loginRequestDTO);

    /**
     * Changes the password of a user with the provided change password request.
     *
     * @param changePasswordRequestDTO the change password request to process
     * @throws RuntimeException if the old password does not match
     */
    void changePassword(ChangePasswordRequestDTO changePasswordRequestDTO);

    /**
     * Recovers the password of a user with the provided recover request.
     *
     * @param recoverRequestDTO the recover request to process
     * @return a response containing the user's name and new password if recovery is successful, or null otherwise
     */
    ResponseDTO forgotPassword(RecoverRequestDTO recoverRequestDTO);
}
