package com.spotify.game.service;

import com.spotify.game.model.entity.Session;
import com.spotify.game.model.entity.User;

public interface SessionService {

    Session createSession(User host);

    void joinSession(User user, String code);

    void updateSession(String code, String playlist, int tracks);

    Session getSessionByCode(String code);

    void deleteSessionByCode(String code);
}
