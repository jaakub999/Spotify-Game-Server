package com.spotify.game.service;

public interface AuthenticationService {

    public boolean authenticate(String username, String password);

    boolean isUserVerified(String username);
}
