package com.spotify.game.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SessionDTO {
    private String host;
    private String code;
    private String playlistId;
    private Integer tracks;
    private List<Long> playerIds;
}