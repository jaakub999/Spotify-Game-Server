package com.spotify.game.service.impl;

import com.spotify.game.model.entity.User;
import com.spotify.game.service.AuthenticationService;
import com.spotify.game.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;

    @Autowired
    public AuthenticationServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean authenticate(String username, String password) {
        Optional<User> userOptional = userService.getUserByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String passwordHash = user.getPasswordHash();

            return BCrypt.checkpw(password, passwordHash);
        } else return false;
    }

    @Override
    public boolean isUserVerified(String username) {
        Optional<User> user = userService.getUserByUsername(username);
        return user.map(User::isVerified).orElse(false);
    }
}