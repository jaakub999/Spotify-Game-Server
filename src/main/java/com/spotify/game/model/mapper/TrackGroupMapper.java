package com.spotify.game.model.mapper;

import com.spotify.game.model.dto.TrackGroupDTO;
import com.spotify.game.model.entity.TrackGroup;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.spotify.game.model.mapper.InternalTrackMapper.mapTrackListToDto;
import static com.spotify.game.model.mapper.SessionMapper.mapSessionToDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TrackGroupMapper {
    public static TrackGroupDTO mapTrackGroupToDto(TrackGroup source) {
        return TrackGroupDTO.builder()
                .tracks(mapTrackListToDto(source.getTracks()))
                .session(mapSessionToDto(source.getSession()))
                .build();
    }

    public static List<TrackGroupDTO> mapTrackGroupsListToDto(List<TrackGroup> source) {
        List<TrackGroupDTO> trackGroupDTOs = new ArrayList<>();

        for (TrackGroup group : source) {
            TrackGroupDTO dto = mapTrackGroupToDto(group);
            trackGroupDTOs.add(dto);
        }

        return trackGroupDTOs;
    }
}
