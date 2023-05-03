package com.spotify.game.service.impl;

import com.spotify.game.exception.SgRuntimeException;
import com.spotify.game.model.entity.User;
import com.spotify.game.security.JwtTokenProvider;
import com.spotify.game.service.AuthenticationService;
import com.spotify.game.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.spotify.game.exception.ExceptionCode.E012;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final JwtTokenProvider provider;

    @Autowired
    public AuthenticationServiceImpl(UserService userService,
                                     JwtTokenProvider provider) {
        this.userService = userService;
        this.provider = provider;
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

    @Override
    public User getUserByToken(@NotNull String token) {
        Claims claims = provider.parseToken(token);
        Long id = Long.parseLong(claims.getSubject());
        Optional<User> user = userService.getUserById(id);

        if (user.isPresent())
            return user.get();

        else throw new SgRuntimeException(E012);
    }
}