package com.spotify.game.service;

import com.spotify.game.model.entity.User;
import jakarta.validation.constraints.NotNull;

public interface AuthenticationService {

    boolean authenticate(String username, String password);

    boolean isUserVerified(String username);

    User getUserByToken(@NotNull String token);
}
