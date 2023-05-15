package com.spotify.game.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InternalTrackDTO {
    private String uri;
    private String name;
    private String artist;
    private Boolean played;
    private TrackGroupDTO trackGroup;
}
