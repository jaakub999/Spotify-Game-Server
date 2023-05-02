package com.spotify.game.rest;

import com.spotify.game.model.dto.SessionDTO;
import com.spotify.game.model.dto.UserDTO;
import com.spotify.game.model.mapper.SessionMapper;
import com.spotify.game.model.entity.Session;
import com.spotify.game.model.entity.User;
import com.spotify.game.service.SessionService;
import com.spotify.game.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("${apiPrefix}/session")
public class SessionController {

    private final SessionService sessionService;
    private final UserService userService;

    public SessionController(SessionService sessionService, UserService userService) {
        this.sessionService = sessionService;
        this.userService = userService;
    }

    @PostMapping("/create")
    public Session createSession(@RequestBody UserDTO hostDto) {
        User host = userService.getUserByUsername(hostDto.getUsername()).orElseThrow(() ->
                new ResponseStatusException(NOT_FOUND, "User not found with username: " + hostDto.getUsername()));
        return sessionService.createSession(host);
    }

    @GetMapping
    public ResponseEntity<List<SessionDTO>> getAllSessions() {
        List<Session> sessions = sessionService.getAllSessions();
        return ResponseEntity.ok(SessionMapper.mapSessionsListToDto(sessions));
    }

    @GetMapping("/{code}")
    public ResponseEntity<SessionDTO> getSession(@PathVariable String code) {
        Session session = sessionService.getSessionByCode(code)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Session not found with code: " + code));
        SessionDTO dto = SessionMapper.mapSessionToDto(session);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{code}/join")
    public ResponseEntity<SessionDTO> joinSession(@RequestBody UserDTO userDto, @PathVariable String code) {
        User user = userService.getUserByUsername(userDto.getUsername()).orElseThrow(() ->
                new ResponseStatusException(NOT_FOUND, "User not found with username: " + userDto.getUsername()));

        sessionService.joinSession(user, code);

        Optional<Session> session = sessionService.getSessionByCode(code);

        if (session.isPresent()) {
            SessionDTO dto = SessionMapper.mapSessionToDto(session.get());
            return ResponseEntity.ok(dto);
        } else {
            throw new ResponseStatusException(NOT_FOUND, "Session not found with code: " + code);
        }
    }
}
