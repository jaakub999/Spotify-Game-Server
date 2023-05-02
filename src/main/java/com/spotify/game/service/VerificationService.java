package com.spotify.game.service;

import com.spotify.game.helper.EmailType;
import com.spotify.game.model.entity.VerificationToken;

public interface VerificationService {

    VerificationToken generateVerificationToken(String email);

    void verifyToken(String token, EmailType emailType);

    String getEmailByToken(String token);
}
