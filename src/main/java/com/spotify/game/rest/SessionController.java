package com.spotify.game.rest;

import com.spotify.game.model.dto.SessionDTO;
import com.spotify.game.model.entity.Session;
import com.spotify.game.model.entity.User;
import com.spotify.game.request.SessionRequest;
import com.spotify.game.response.SessionResponse;
import com.spotify.game.service.AuthenticationService;
import com.spotify.game.service.SessionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import static com.spotify.game.model.mapper.SessionMapper.mapSessionToDto;
import static com.spotify.game.security.SecurityConstants.HEADER;

@AllArgsConstructor
@RestController
@RequestMapping("${apiPrefix}/session")
public class SessionController {

    private final SessionService sessionService;
    private final AuthenticationService authenticationService;
    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping("/create")
    public ResponseEntity<SessionDTO> createSession(@RequestHeader(HEADER) String token) {
        User user = authenticationService.getUserByToken(token);
        Session session = sessionService.createSession(user);

        return ResponseEntity.ok(mapSessionToDto(session));
    }

    @GetMapping("/{code}")
    public ResponseEntity<SessionResponse> getSession(@RequestHeader(HEADER) String token, @PathVariable String code) {
        User user = authenticationService.getUserByToken(token);
        Session session = sessionService.getSessionByCode(code);
        return ResponseEntity.ok(new SessionResponse(mapSessionToDto(session), user.getUsername()));
    }

    @PostMapping("/{code}/join")
    public void joinSession(@RequestHeader(HEADER) String token, @PathVariable String code) {
        User user = authenticationService.getUserByToken(token);
        sessionService.joinSession(user, code);
    }

    @PostMapping("/update")
    public void updateSessionData(@RequestBody SessionRequest request) {
        sessionService.updateSession(
                request.getSession(),
                request.getPlaylist(),
                request.getTracks()
        );
    }

    @DeleteMapping("/delete")
    public void deleteSession(@RequestParam String code) {
        sessionService.deleteSessionByCode(code);
    }
}
