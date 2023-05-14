package com.spotify.game.service;

import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;

import java.util.List;

public interface SpotifyService {

    List<PlaylistSimplified> getPlaylistsByName(String query);

    List<PlaylistTrack> getPlaylistTracksById(String id, int number);
}
