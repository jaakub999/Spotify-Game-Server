package com.spotify.game.rest;

import com.spotify.game.request.TracksRequest;
import com.spotify.game.service.SpotifyService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;

import java.util.List;

@RestController
@RequestMapping("${apiPrefix}/spotify")
@AllArgsConstructor
public class SpotifyController {

    private final SpotifyService spotifyService;

    @GetMapping("/playlists")
    public List<PlaylistSimplified> searchPlaylists(@RequestParam("q") String query) {
        return spotifyService.getPlaylistsByName(query);
    }

    @GetMapping("/playlist")
    public List<PlaylistTrack> getPlaylistTracks(@RequestBody @Valid TracksRequest request) {
        String id = request.getId();
        int number = request.getNumber();

        return spotifyService.getPlaylistTracksById(id, number);
    }
}