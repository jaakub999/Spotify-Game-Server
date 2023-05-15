package com.spotify.game.service;

import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.util.List;
import java.util.Map;

public interface SpotifyService {

    List<PlaylistSimplified> getPlaylists(String query);

    Map<Integer, Map<Track, Boolean>> getPlaylistTracks(String id, int number);
}
