package com.spotify.game.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExceptionCode {
    E001("User not found"),
    E002("Failed to get user playlists"),
    E003("Playlist not found"),
    E004("Session not found"),
    E005("Token not found"),
    E006("Token has expired"),
    E007("User is already verified"),
    E008("User with that username or email already exists"),
    E009("Invalid email address"),
    E010("Passwords do not match"),
    E011("JWT token date expired"),
    E012("JWT token is invalid"),
    E013("An error occurred during fetching Spotify tracks");

    private final String message;
}
