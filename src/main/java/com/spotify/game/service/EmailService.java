package com.spotify.game.service;

import com.spotify.game.model.entity.User;

public interface EmailService {

    void sendVerificationEmail(User user);
}