package com.spotify.game.model.mapper;

import com.spotify.game.model.dto.SessionDTO;
import com.spotify.game.model.entity.Session;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.spotify.game.model.mapper.UserMapper.mapUsersListToDto;
import static com.spotify.game.model.mapper.TrackGroupMapper.mapTrackGroupsListToDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SessionMapper {
    public static SessionDTO mapSessionToDto(Session source) {
        return SessionDTO.builder()
                .host(source.getHost())
                .code(source.getCode())
                .playlistId(source.getPlaylistId())
                .active(source.getActive())
                .date(source.getDate())
                .players(mapUsersListToDto(source.getPlayers()))
                .trackGroups(mapTrackGroupsListToDto(source.getTrackGroups()))
                .build();
    }
}
