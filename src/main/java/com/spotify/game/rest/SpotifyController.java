package com.spotify.game.rest;

import com.spotify.game.service.SpotifyService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("${apiPrefix}/spotify")
public class SpotifyController {

    private final SpotifyService spotifyService;

    @GetMapping("/playlists")
    public List<PlaylistSimplified> searchPlaylists(@RequestParam("q") String query) {
        return spotifyService.getPlaylists(query);
    }
}