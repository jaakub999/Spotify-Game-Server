package com.spotify.game.service.impl;

import com.spotify.game.exception.SgRuntimeException;
import com.spotify.game.model.entity.Session;
import com.spotify.game.model.entity.User;
import com.spotify.game.repository.SessionRepository;
import com.spotify.game.repository.UserRepository;
import com.spotify.game.service.SessionService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

import static com.spotify.game.exception.ExceptionCode.E004;

@Service
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final EntityManager entityManager;

    @Autowired
    public SessionServiceImpl(SessionRepository sessionRepository,
                              UserRepository userRepository,
                              EntityManager entityManager) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public Session createSession(User host) {
        Session session = new Session();
        String code;
        boolean codeExists;

        do {
            code = generateCode();
            Optional<Session> existingSession = sessionRepository.findByCode(code);
            codeExists = existingSession.isPresent();
        } while (codeExists);

        host = entityManager.merge(host);
        session.setHost(host.getUsername());
        session.setCode(code);
        session.getPlayers().add(host);
        session.setStarted(false);

        host.setSession(session);
        userRepository.save(host);

        sessionRepository.save(session);

        return session;
    }

    @Override
    public Optional<Session> getSessionByCode(String code) {
        return Optional.ofNullable(sessionRepository.findByCode(code)
                .orElseThrow(() -> new SgRuntimeException(E004)));
    }

    @Override
    @Transactional
    public Session joinSession(User user, String code) {
        Optional<Session> session = sessionRepository.findByCode(code);

        if (session.isPresent()) {
            Session currentSession = session.get();
            user.setSession(currentSession);
            session.get().getPlayers().add(user);
            userRepository.save(user);
            sessionRepository.save(currentSession);

            return session.get();
        }

        else throw new SgRuntimeException(E004);
    }

    private String generateCode() {
        var alphanumericChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        var length = 5;
        var random = new SecureRandom();
        var sb = new StringBuilder(length);

        for (var i = 0; i < length; i++) {
            var randomIndex = random.nextInt(alphanumericChars.length());
            sb.append(alphanumericChars.charAt(randomIndex));
        }

        return sb.toString();
    }
}
