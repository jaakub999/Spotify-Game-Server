package com.spotify.game.rest;

import com.spotify.game.service.SpotifyService;
import org.springframework.web.bind.annotation.*;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;

import java.util.List;

@RestController
@RequestMapping("/spotify")
public class SpotifyController {
    private final SpotifyService spotifyService;

    public SpotifyController(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }

    @GetMapping("/playlists")
    public List<PlaylistSimplified> searchPlaylists(@RequestParam("q") String query) {
        return spotifyService.getPlaylistsByName(query);
    }
}