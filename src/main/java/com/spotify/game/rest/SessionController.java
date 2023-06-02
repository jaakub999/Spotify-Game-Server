package com.spotify.game.rest;

import com.spotify.game.model.dto.SessionDTO;
import com.spotify.game.model.dto.TrackGroupDTO;
import com.spotify.game.model.entity.InternalTrack;
import com.spotify.game.model.entity.Session;
import com.spotify.game.model.entity.TrackGroup;
import com.spotify.game.model.entity.User;
import com.spotify.game.request.SessionRequest;
import com.spotify.game.response.SessionResponse;
import com.spotify.game.service.AuthenticationService;
import com.spotify.game.service.SessionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.spotify.game.model.mapper.SessionMapper.mapSessionToDto;
import static com.spotify.game.model.mapper.TrackGroupMapper.mapTrackGroupsListToDto;
import static com.spotify.game.security.SecurityConstants.HEADER;

@AllArgsConstructor
@RestController
@RequestMapping("${apiPrefix}/session")
public class SessionController {

    private final SessionService sessionService;
    private final AuthenticationService authenticationService;

    @PostMapping("/create")
    public ResponseEntity<SessionDTO> createSession(@RequestHeader(HEADER) String token) {
        User user = authenticationService.getUserByToken(token);
        Session session = sessionService.createSession(user);

        return ResponseEntity.ok(mapSessionToDto(session));
    }

    @GetMapping("/{code}")
    public ResponseEntity<SessionResponse> getSession(@RequestHeader(HEADER) String token, @PathVariable String code) {
        User user = authenticationService.getUserByToken(token);
        Session session = sessionService.getSession(code);
        return ResponseEntity.ok(new SessionResponse(mapSessionToDto(session), user.getUsername()));
    }

    @PostMapping("/{code}/join")
    public void joinSession(@RequestHeader(HEADER) String token, @PathVariable String code) {
        User user = authenticationService.getUserByToken(token);
        sessionService.joinSession(user, code);
    }

    @GetMapping("/{code}/tracks")
    public List<String> getSessionTracks(@PathVariable String code) {
        List<TrackGroup> trackGroups = sessionService.getSessionTrackGroups(code);
        List<String> tracks = new ArrayList<>();

        for (TrackGroup trackGroup : trackGroups) {
            for (var j = 0; j < trackGroup.getTracks().size(); j++) {
                InternalTrack track = trackGroup.getTracks().get(j);

                if (track.getPlayed())
                    tracks.add(track.getName() + " - " + track.getArtist());
            }
        }

        return tracks;
    }

    @PostMapping("/update")
    public void updateSessionData(@RequestBody @Valid SessionRequest request) {
        sessionService.updateSession(
                request.getSessionCode(),
                request.getPlaylistId(),
                request.getTracksNumber()
        );
    }

    @PostMapping("/deactivate")
    public void deactivateSession(@RequestParam String code) {
        sessionService.deactivateSession(code);
    }
}
