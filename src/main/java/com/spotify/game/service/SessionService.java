package com.spotify.game.service;

import com.spotify.game.model.entity.Session;
import com.spotify.game.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface SessionService {

    Session createSession(User host);

    Optional<Session> getSessionByCode(String code);

    List<Session> getAllSessions();

    void joinSession(User user, String code);

    void deleteSessionById(Long id);
}
