package com.spotify.game.service;

import com.spotify.game.model.entity.Session;
import com.spotify.game.model.entity.TrackGroup;
import com.spotify.game.model.entity.User;

import java.util.List;

public interface SessionService {

    Session createSession(User host);

    Session getSession(String code);

    void joinSession(User user, String code);

    void updateSession(String code, String playlistId, int number);

    List<TrackGroup> getSessionTrackGroups(String code);

    void deactivateSession(String code);
}
