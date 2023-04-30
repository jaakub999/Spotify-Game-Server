package com.spotify.game.service;

import com.spotify.game.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User createUser(String username, String password, String email);

    List<User> getAllUsers();

    Optional<User> getUserById(Long id);

    Optional<User> getUserByUsername(String username);

    Optional<User> getUserByEmail(String email);

    void deleteUserById(Long id);

    void changePassword(String username, String currentPassword, String newPassword);
}
