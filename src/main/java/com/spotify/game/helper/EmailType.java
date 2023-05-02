package com.spotify.game.helper;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmailType {
    REGISTER("Verify your email address"),
    PASSWORD("Change your password");

    private final String subject;
}