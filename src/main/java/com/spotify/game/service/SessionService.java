package com.spotify.game.service;

import com.spotify.game.model.entity.Session;
import com.spotify.game.model.entity.User;

public interface SessionService {

    Session createSession(User host);

    void joinSession(User user, String code);

    void updateSession(String code, String playlistId, int number);

    Session getSession(String code);

    void deactivateSession(String code);
}
