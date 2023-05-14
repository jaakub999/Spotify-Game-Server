package com.spotify.game.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpotifyServiceTest {

    @Autowired
    SpotifyService spotifyService;

    @Test
    public void getPlaylistsByNameWithValidQueryTest() {
        List<PlaylistSimplified> playlists = spotifyService.getPlaylistsByName("rock");
        assertNotNull(playlists);
        assertFalse(playlists.isEmpty());
    }

    @Test
    public void getPlaylistsByNameWithEmptyQueryTest() {
        List<PlaylistSimplified> playlists = spotifyService.getPlaylistsByName("");
        assertNotNull(playlists);
        assertTrue(playlists.isEmpty());
    }

    @Test
    public void getPlaylistsByNameWithNullQueryTest() {
        List<PlaylistSimplified> playlists = spotifyService.getPlaylistsByName(null);
        assertNotNull(playlists);
        assertTrue(playlists.isEmpty());
    }

    @Test
    public void getPlaylistTracksTest() {
        String id = "37i9dQZF1EQpj7X7UK8OOF";
        int number = 10;
        List<PlaylistTrack> tracks = spotifyService.getPlaylistTracksById(id, number);
        assertNotNull(tracks);
        assertFalse(tracks.isEmpty());
    }
}