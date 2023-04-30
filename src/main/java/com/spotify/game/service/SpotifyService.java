package com.spotify.game.service;

import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;

import java.util.List;

public interface SpotifyService {
    List<PlaylistSimplified> getPlaylistsByName(String query);
}
