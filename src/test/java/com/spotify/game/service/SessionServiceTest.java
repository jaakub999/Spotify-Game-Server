package com.spotify.game.service;

import com.spotify.game.exception.SgRuntimeException;
import com.spotify.game.model.entity.Session;
import com.spotify.game.model.entity.User;
import com.spotify.game.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SessionServiceTest {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void createSessionTest() {
        User user = new User();
        user.setUsername("testuserforsessiontests");
        user.setPasswordHash("password");
        user.setEmail("testuserforsessiontests@test.com");
        userRepository.save(user);

        Session session = sessionService.createSession(user);
        String code = session.getCode();
        Optional<Session> retrievedSession = sessionService.getSessionByCode(code);

        assertTrue(retrievedSession.isPresent());
        assertEquals(session.getId(), retrievedSession.get().getId());
        assertEquals(user.getUsername(), session.getHost());
        assertEquals(1, session.getPlayers().size());
    }

    @Test
    public void getSessionByCodeTest() {
        String nonexistentCode = "12345";

        assertThrows(SgRuntimeException.class, () -> sessionService.getSessionByCode(nonexistentCode));
    }

    @Test
    public void joinSessionTest() {
        User host = new User();
        host.setUsername("testuserforsessiontests1");
        host.setPasswordHash("password");
        host.setEmail("testuserforsessiontests1@test.com");
        userRepository.save(host);

        User player = new User();
        player.setUsername("testuserforsessiontests2");
        player.setPasswordHash("password2");
        player.setEmail("testuserforsessiontests2@test.com");
        userRepository.save(player);

        Session session = sessionService.createSession(host);
        sessionService.joinSession(player, session.getCode());
        session = sessionService.getSessionByCode(session.getCode()).get();

        assertEquals(2, session.getPlayers().size());
    }
}
