package com.spotify.game.service;

import com.spotify.game.exception.SgRuntimeException;
import com.spotify.game.model.entity.InternalTrack;
import com.spotify.game.model.entity.Session;
import com.spotify.game.model.entity.TrackGroup;
import com.spotify.game.model.entity.User;
import com.spotify.game.repository.SessionRepository;
import com.spotify.game.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SessionServiceTest {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void createSessionTest() {
        User user = User.builder()
                .username("testuserforsessiontests")
                .email("testuserforsessiontests@test.com")
                .verified(true)
                .passwordHash("password")
                .build();

        userRepository.save(user);

        Session session = sessionService.createSession(user);
        String code = session.getCode();
        Optional<Session> retrievedSession = sessionRepository.findByCode(code);

        assertTrue(retrievedSession.isPresent());
        assertEquals(session.getId(), retrievedSession.get().getId());
        assertEquals(user.getUsername(), session.getHost());
        assertEquals(1, session.getPlayers().size());
    }

    @Test
    public void joinSessionTest() {
        User host = User.builder()
                .username("testuserforsessiontests1")
                .email("testuserforsessiontests1@test.com")
                .verified(true)
                .passwordHash("password")
                .build();

        User player = User.builder()
                .username("testuserforsessiontests2")
                .email("testuserforsessiontests2@test.com")
                .verified(true)
                .passwordHash("password2")
                .build();

        userRepository.save(host);
        userRepository.save(player);

        Session session = sessionService.createSession(host);
        sessionService.joinSession(player, session.getCode());
        session = sessionRepository.findByCode(session.getCode()).get();

        assertEquals(2, session.getPlayers().size());
    }

    @Test
    public void getSessionByCodeTest() {
        final String nonexistentCode = "1234567";

        assertThrows(SgRuntimeException.class, () -> sessionService.getSession(nonexistentCode));
    }

    @Test
    public void updateSessionTest() {
        final String playlistId = "37i9dQZF1EQpj7X7UK8OOF";
        final int number = 3;
        User user = User.builder()
                .username("testuserforsessiontests3")
                .email("testuserforsessiontests3@test.com")
                .verified(true)
                .passwordHash("password3")
                .build();

        userRepository.save(user);

        Session session = sessionService.createSession(user);
        String code = session.getCode();
        sessionService.updateSession(code, playlistId, number);
        session = sessionService.getSession(code);

        assertNotNull(session.getPlaylistId());
        assertFalse(session.getTrackGroups().isEmpty());
    }
}
