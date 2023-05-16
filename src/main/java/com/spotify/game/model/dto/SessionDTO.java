package com.spotify.game.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class SessionDTO {
    private String host;
    private String code;
    private String playlistId;
    private Boolean active;
    private LocalDateTime date;
    private List<UserDTO> players;
    private List<TrackGroupDTO> trackGroups;
}