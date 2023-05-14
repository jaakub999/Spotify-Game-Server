package com.spotify.game.model.mapper;

import com.spotify.game.model.dto.SessionDTO;
import com.spotify.game.model.entity.Session;
import com.spotify.game.model.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SessionMapper {
    public static SessionDTO mapSessionToDto(Session source) {
        return SessionDTO.builder()
                .host(source.getHost())
                .code(source.getCode())
                .playlistId(source.getPlaylistId())
                .tracks(source.getTracks())
                .playerIds(source.getPlayers().stream().map(User::getId).collect(Collectors.toList()))
                .build();
    }

    public static List<SessionDTO> mapSessionsListToDto(List<Session> source) {
        List<SessionDTO> sessionDTOs = new ArrayList<>();

        for (Session session : source) {
            SessionDTO dto = mapSessionToDto(session);
            sessionDTOs.add(dto);
        }

        return sessionDTOs;
    }
}
