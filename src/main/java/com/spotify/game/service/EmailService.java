package com.spotify.game.service;

import com.spotify.game.helper.EmailType;
import com.spotify.game.model.entity.User;

public interface EmailService {

    void sendEmail(User user, EmailType type);
}