package com.spotify.game.model.mapper;

import com.spotify.game.model.dto.InternalTrackDTO;
import com.spotify.game.model.entity.InternalTrack;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.spotify.game.model.mapper.TrackGroupMapper.mapTrackGroupToDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InternalTrackMapper {
    public static InternalTrackDTO mapTrackToTrackDto(InternalTrack source) {
        return InternalTrackDTO.builder()
                .uri(source.getUri())
                .name(source.getName())
                .artist(source.getArtist())
                .played(source.getPlayed())
                .trackGroup(mapTrackGroupToDto(source.getTrackGroup()))
                .build();
    }

    public static List<InternalTrackDTO> mapTrackListToDto(List<InternalTrack> source) {
        List<InternalTrackDTO> trackDTOs = new ArrayList<>();

        for (InternalTrack track : source) {
            InternalTrackDTO dto = mapTrackToTrackDto(track);
            trackDTOs.add(dto);
        }

        return trackDTOs;
    }
}
