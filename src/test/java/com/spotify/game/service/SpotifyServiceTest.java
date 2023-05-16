package com.spotify.game.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpotifyServiceTest {

    @Autowired
    private SpotifyService spotifyService;

    @Autowired
    private SpotifyOAuthService spotifyOAuthService;

    @Test
    public void getPlaylistsByNameWithValidQueryTest() {
        List<PlaylistSimplified> playlists = spotifyService.getPlaylists("rock");

        assertNotNull(playlists);
        assertFalse(playlists.isEmpty());
    }

    @Test
    public void getPlaylistsByNameWithEmptyQueryTest() {
        List<PlaylistSimplified> playlists = spotifyService.getPlaylists("");

        assertNotNull(playlists);
        assertTrue(playlists.isEmpty());
    }

    @Test
    public void getPlaylistsByNameWithNullQueryTest() {
        List<PlaylistSimplified> playlists = spotifyService.getPlaylists(null);

        assertNotNull(playlists);
        assertTrue(playlists.isEmpty());
    }

    @Test
    public void getPlaylistTracksTest() {
        final String id = "37i9dQZF1EQpj7X7UK8OOF";
        final int number = 5;
        Map<Integer, Map<Track, Boolean>> tracks = spotifyService.getPlaylistTracks(id, number);

        assertNotNull(tracks);
        assertFalse(tracks.isEmpty());
    }

    @Test
    public void getAccessTokenTest() {
        String token = spotifyOAuthService.getAccessToken();

        assertNotNull(token);
    }
}