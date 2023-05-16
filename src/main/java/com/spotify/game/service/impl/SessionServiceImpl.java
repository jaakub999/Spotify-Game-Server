package com.spotify.game.service.impl;

import com.spotify.game.exception.SgRuntimeException;
import com.spotify.game.model.entity.Session;
import com.spotify.game.model.entity.InternalTrack;
import com.spotify.game.model.entity.TrackGroup;
import com.spotify.game.model.entity.User;
import com.spotify.game.repository.InternalTrackRepository;
import com.spotify.game.repository.SessionRepository;
import com.spotify.game.repository.TrackGroupRepository;
import com.spotify.game.repository.UserRepository;
import com.spotify.game.service.SessionService;
import com.spotify.game.service.SpotifyService;
import com.spotify.game.service.WebSocketService;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.spotify.game.exception.ExceptionCode.E004;

@Slf4j
@Service
public class SessionServiceImpl implements SessionService {

    private final WebSocketService webSocketService;
    private final SpotifyService spotifyService;
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final InternalTrackRepository internalTrackRepository;
    private final TrackGroupRepository trackGroupRepository;
    private final EntityManager entityManager;

    @Autowired
    public SessionServiceImpl(WebSocketService webSocketService,
                              SpotifyService spotifyService,
                              SessionRepository sessionRepository,
                              UserRepository userRepository,
                              InternalTrackRepository internalTrackRepository,
                              TrackGroupRepository trackGroupRepository,
                              EntityManager entityManager) {
        this.webSocketService = webSocketService;
        this.spotifyService = spotifyService;
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
        this.internalTrackRepository = internalTrackRepository;
        this.trackGroupRepository = trackGroupRepository;
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public Session createSession(User host) {
        String code;
        boolean codeExists;

        do {
            code = generateCode();
            Optional<Session> existingSession = sessionRepository.findByCode(code);
            codeExists = existingSession.isPresent();
        } while (codeExists);

        host = entityManager.merge(host);

        Session session = Session.builder()
                .host(host.getUsername())
                .code(code)
                .active(true)
                .date(LocalDateTime.now())
                .players(new ArrayList<>())
                .trackGroups(new ArrayList<>())
                .build();

        session.getPlayers().add(host);
        host.setSession(session);
        userRepository.save(host);
        sessionRepository.save(session);

        return session;
    }

    @Override
    public Session getSession(String code) {
        return sessionRepository.findByCode(code)
                .orElseThrow(() -> new SgRuntimeException(E004));
    }

    @Override
    @Transactional
    public void joinSession(User user, String code) {
        Session session = getSession(code);
        user.setSession(session);
        session.getPlayers().add(user);
        userRepository.save(user);
        sessionRepository.save(session);
        String topic = "session/join/" + code;
        notifyFrontend(topic);
    }

    @Override
    @Transactional
    public void updateSession(String code, String playlistId, int number) {
        Session session = getSession(code);
        Map<Integer, Map<Track, Boolean>> tracks = spotifyService.getPlaylistTracks(playlistId, number);
        List<TrackGroup> trackGroups = createTrackGroups(tracks, session);
        session.setPlaylistId(playlistId);
        session.setTrackGroups(trackGroups);
        sessionRepository.save(session);
        String topic = "session/start/" + code;
        notifyFrontend(topic);
    }

    @Override
    public List<TrackGroup> getSessionTrackGroups(String code) {
        Session session = getSession(code);
        return session.getTrackGroups();
    }

    @Override
    public void deactivateSession(String code) {
        Session session = getSession(code);
        session.setActive(false);
        sessionRepository.save(session);
        String topic = "session/delete/" + code;
        notifyFrontend(topic);
    }

    private String generateCode() {
        final String alphanumericChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        final int length = 6;
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);

        for (var i = 0; i < length; i++) {
            int randomIndex = random.nextInt(alphanumericChars.length());
            sb.append(alphanumericChars.charAt(randomIndex));
        }

        return sb.toString();
    }

    private List<TrackGroup> createTrackGroups(Map<Integer, Map<Track, Boolean>> tracks, Session session) {
        List<TrackGroup> trackGroups = new ArrayList<>();

        for (Map.Entry<Integer, Map<Track, Boolean>> outerEntry : tracks.entrySet()) {
            List<InternalTrack> internalTracks = new ArrayList<>();
            Map<Track, Boolean> innerMap = outerEntry.getValue();
            TrackGroup group = new TrackGroup();

            for (Map.Entry<Track, Boolean> innerEntry : innerMap.entrySet()) {
                Track track = innerEntry.getKey();
                Boolean value = innerEntry.getValue();
                ArtistSimplified[] artists = track.getArtists();
                InternalTrack internalTrack = InternalTrack.builder()
                        .uri(track.getUri())
                        .name(track.getName())
                        .artist(artists[0].getName())
                        .played(value)
                        .build();

                group.getTracks().add(internalTrack);
                internalTrack.setTrackGroup(group);
                internalTracks.add(internalTrack);
            }

            group.setSession(session);
            internalTrackRepository.saveAll(internalTracks);
            trackGroups.add(group);
        }

        trackGroupRepository.saveAll(trackGroups);

        return trackGroups;
    }

    private void notifyFrontend(String topic) {
        if (topic == null) {
            log.error("WebSocket: Failed to get entity topic");
            return;
        }

        webSocketService.sendMessage(topic);
        log.info("WebSocket: /topic/" + topic);
    }
}
