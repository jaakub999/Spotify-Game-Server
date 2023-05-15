package com.spotify.game.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TrackGroupDTO {
    private List<InternalTrackDTO> tracks;
    private SessionDTO session;
}
