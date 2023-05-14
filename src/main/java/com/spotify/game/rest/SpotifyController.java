package com.spotify.game.rest;

import com.spotify.game.service.SpotifyService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("${apiPrefix}/spotify")
public class SpotifyController {

    private final SpotifyService spotifyService;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("/playlists")
    public List<PlaylistSimplified> searchPlaylists(@RequestParam("q") String query) {
        return spotifyService.getPlaylistsByName(query);
    }

    @GetMapping("/tracks/random")
    public void getPlaylistTracks(@RequestParam String id, @RequestParam int number) {
        List<PlaylistTrack> tracks = spotifyService.getPlaylistTracksById(id, number);

        messagingTemplate.convertAndSend("topic/spotify/random_tracks", tracks);
    }
}